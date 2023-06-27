package com.sas.ims.service.serviceImpl;

import java.time.LocalDateTime;

import com.sas.ims.assembler.SalesOrderAssembler;
import com.sas.ims.constant.Constant;
import com.sas.ims.dao.SalesOrderDao;
import com.sas.ims.dto.RequestProductMapping;
import com.sas.ims.dto.SalesOrderDto;
import com.sas.ims.dto.SalesOrderItemDto;
import com.sas.ims.entity.ProductMaster;
import com.sas.ims.entity.RequestOrder;
import com.sas.ims.entity.SalesOrder;
import com.sas.ims.entity.SalesOrderItem;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.repository.ProductMasterRepository;
import com.sas.ims.repository.RequestOrderRepository;
import com.sas.ims.repository.SalesOrderItemRepository;
import com.sas.ims.repository.SalesOrderRepository;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.request.SalesOrderRequest;
import com.sas.ims.service.SalesOrderService;
import com.sas.ims.utils.DateTimeUtil;
import com.sas.ims.utils.ExcelGeneratorUtil;
import com.sas.tokenlib.response.Response;
import com.sas.tokenlib.service.UserCredentialService;
import com.sas.tokenlib.utils.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class SalesOrderServiceImpl implements SalesOrderService, Constant {
    private final UserCredentialService userCredentialService;
    private final RequestOrderRepository requestOrderRepository;
    private final ProductMasterRepository productMasterRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final SalesOrderTransactionService salesOrderTransactionService;
    private final SalesOrderDao salesOrderDao;
    private final CommonUtil commonUtil;
    private final ExcelGeneratorUtil excelGeneratorUtil;
    private final SalesOrderAssembler salesOrderAssembler;

    @Override
    public Response getSalesOrder(Long salesOrderId) throws ObjectNotFoundException {
        var userSession = userCredentialService.getUserSession();
        var salesOrder = salesOrderRepository.findById(salesOrderId);
        if (salesOrder.isPresent()) {
            var orderProducts = salesOrderItemRepository.findByOrgIdAndSalesOrderIdIn(userSession.getCompany().getCompanyId(), List.of(salesOrderId));
            if (orderProducts.isPresent()) {
                var orderAmount = orderProducts.get().stream()
                        .map(SalesOrderItem::getProductAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                var salesOrderDto = new SalesOrderDto();
                salesOrderDto.setId(salesOrder.get().getId());
                salesOrderDto.setSalesOrderId(salesOrder.get().getSalesOrderId());
                salesOrderDto.setSalesOrderNumber(salesOrder.get().getSalesOrderNumber());
                salesOrderDto.setBatchId(salesOrder.get().getBatchId());
                salesOrderDto.setCreatedBy(salesOrder.get().getInsertedBy());
                salesOrderDto.setCreatedOn(DateTimeUtil.dateTimeToString(salesOrder.get().getInsertedOn(), DateTimeUtil.DDMMYYYY));
                salesOrderDto.setStatus(salesOrder.get().getStatus());
                salesOrderDto.setOrderAmount(orderAmount);
                salesOrderDto.setOrderType(salesOrder.get().getSalesOrderType());
                return new Response("Transaction completed successfully", salesOrderDto, HttpStatus.OK);
            } else {
                log.info("No products found for orderId: " + salesOrderId);
                throw new ObjectNotFoundException("No products found for orderId: " + salesOrderId);
            }
        } else {
            log.info("No sales order found for Id: " + salesOrderId);
            throw new ObjectNotFoundException("No sales order found for Id: " + salesOrderId);
        }
    }

    @Override
    public Response getSalesOrderList(FilterRequest filterRequest) {
        var userSession = userCredentialService.getUserSession();
        var salesOrderDtoList = new ArrayList<SalesOrderDto>();
        try {
            var salesOrders = salesOrderDao.findAllSalesOrder(filterRequest, userSession);
            var totalCount = salesOrderDao.findAllSalesOrderCount(filterRequest, userSession);
            if (salesOrders != null && !salesOrders.isEmpty()) {
                salesOrders.forEach(salesOrder -> {
                    var orderProducts = salesOrderItemRepository.findByOrgIdAndSalesOrderIdIn(userSession.getCompany().getCompanyId(), List.of(salesOrder.getId()));
                    if (orderProducts.isPresent()) {
                        var orderAmount = orderProducts.get().stream()
                                .map(SalesOrderItem::getProductAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        var salesOrderDto = new SalesOrderDto();
                        salesOrderDto.setId(salesOrder.getId());
                        salesOrderDto.setSalesOrderId(salesOrder.getSalesOrderId());
                        salesOrderDto.setSalesOrderNumber(salesOrder.getSalesOrderNumber());
                        salesOrderDto.setBatchId(salesOrder.getBatchId());
                        salesOrderDto.setCreatedBy(salesOrder.getInsertedBy());
                        salesOrderDto.setCreatedOn(DateTimeUtil.dateTimeToString(salesOrder.getInsertedOn(), DateTimeUtil.DDMMYYYY));
                        salesOrderDto.setStatus(salesOrder.getStatus());
                        salesOrderDto.setOrderAmount(orderAmount);
                        salesOrderDto.setOrderType(salesOrder.getSalesOrderType());

                        salesOrderDtoList.add(salesOrderDto);
                    }
                });
                return new Response("Transaction completed successfully!", salesOrderDtoList, totalCount, HttpStatus.OK);
            } else {
                return new Response("No records found!", new ArrayList<SalesOrder>(), HttpStatus.OK);
            }
        } catch (Exception exception) {
            log.info("Something went wrong while fetching sales orders. Reason: {}", exception.getMessage());
            return new Response("Something went wrong while fetching sales orders!", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Response createSalesOrder(SalesOrderRequest salesOrderRequest) {
        var userSession = userCredentialService.getUserSession();

        //Validate request
        if (salesOrderRequest.getRequestList() == null || salesOrderRequest.getRequestList().isEmpty()) {
            return new Response("No request order selected for creating sales order!", HttpStatus.BAD_REQUEST);
        }
        if (!StringUtils.hasText(salesOrderRequest.getOrderType()))
            return new Response("Sales order type is mandatory!", HttpStatus.BAD_REQUEST);

        //Create Sales Order
        switch (salesOrderRequest.getOrderType()) {

            case "combined": {
                log.info("Request received for creating a sales order. Order type: {}", salesOrderRequest.getOrderType());
                if (salesOrderRequest.getOrderAmount() == null || salesOrderRequest.getOrderAmount().equals(BigDecimal.ZERO)) {
                    return new Response("Order amount is mandatory for combined order type!", HttpStatus.BAD_REQUEST);
                }

                var batchId = commonUtil.getBatchId(userSession);

                try {
                    var productAmount = new Object() {
                        BigDecimal totalProductAmount = BigDecimal.ZERO;
                    };

                    var requestProductMappingList = new ArrayList<RequestProductMapping>();
                    for (Long request : salesOrderRequest.getRequestList()) {
                        var requestProductMapping = new RequestProductMapping();
                        var requestOrder = requestOrderRepository.findByRequestOrderIdAndOrgIdAndStatus(request, userSession.getCompany().getCompanyId(), "P");
                        if (requestOrder.isPresent()) {
                            requestProductMapping.setRequest(requestOrder.get());
                            var product = productMasterRepository.findByOrgIdAndProductId(userSession.getCompany().getCompanyId(), requestOrder.get().getProductId());
                            if (product.isPresent()) {
                                if (product.get().getIsCombo().equals("Y")) {
                                    var comboProducts = productMasterRepository.findByOrgIdAndParentProductId(userSession.getCompany().getCompanyId(), product.get().getProductId());
                                    if (comboProducts.isPresent()) {
                                        if (!comboProducts.get().isEmpty()) {
                                            requestProductMapping.setProducts(comboProducts.get());
                                        } else {
                                            log.info("No parent product id found for productId: {}", product.get().getProductId());
                                            return new Response("No products found for the selected requests in the system!. RequestID: " + request, HttpStatus.BAD_REQUEST);
                                        }
                                    } else {
                                        log.info("No parent product id found for productId: {}", product.get().getProductId());
                                        return new Response("No products found for the selected requests in the system!. RequestID: " + request, HttpStatus.BAD_REQUEST);
                                    }
                                } else {
                                    requestProductMapping.setProducts(List.of(product.get()));
                                }
                                requestProductMappingList.add(requestProductMapping);
                            } else {
                                log.info("No products found for the given input request order: {} and product: {}", request, requestOrder.get().getProductId());
                                return new Response("No products found for the selected requests in the system!. RequestID: " + request, HttpStatus.BAD_REQUEST);
                            }
                        } else {
                            log.info("No request orders found for the given input request order list: {}", salesOrderRequest.getRequestList());
                            return new Response("No request orders found for the selected requests in the system!", HttpStatus.BAD_REQUEST);
                        }
                    }

                    //Creating combined sales order
                    var salesOrderList = new ArrayList<SalesOrder>();
                    var salesOrderItemsList = new ArrayList<SalesOrderItem>();
                    var requestOrders = new ArrayList<RequestOrder>();

                    for (RequestProductMapping requestProductMapping : requestProductMappingList) {
                        var productAmountPerRequest = requestProductMapping.getProducts().stream()
                                .map(ProductMaster::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        if (productAmountPerRequest.compareTo(salesOrderRequest.getOrderAmount()) > 0) {
                            return new Response("Order amount is less than request order product cumulative amount. Please increase your order amount. Request ID: " + requestProductMapping.getRequest().getRequestOrderId(), HttpStatus.BAD_REQUEST);
                        }

                        productAmount.totalProductAmount = productAmount.totalProductAmount.add(productAmountPerRequest);

                        if (productAmount.totalProductAmount.compareTo(salesOrderRequest.getOrderAmount()) <= 0) {
                            if (salesOrderList.isEmpty()) {
                                var salesOrder = new SalesOrder();
                                var sequence = salesOrderRepository.findLatestSequence();
                                salesOrder.setId(sequence);
                                salesOrder.setSalesOrderId("SO-" + sequence);
                                salesOrder.setSalesOrderNumber("SO-" + sequence);
                                salesOrder.setRequestId("'" + requestProductMapping.getRequest().getRequestOrderId() + "'");
                                salesOrder.setOrgId(userSession.getCompany().getCompanyId());
                                salesOrder.setPktReference(null);
                                salesOrder.setBatchId(batchId);
                                salesOrder.setStatus("P");
                                salesOrder.setSalesOrderType(salesOrderRequest.getOrderType());
                                salesOrder.setInsertedBy(userSession.getSub());
                                salesOrder.setInsertedOn(LocalDateTime.now());

                                salesOrderList.add(salesOrder);

                                requestOrders.add(requestProductMapping.getRequest());

                                for (ProductMaster productMaster : requestProductMapping.getProducts()) {
                                    var salesOrderItem = new SalesOrderItem();
                                    salesOrderItem.setId(salesOrderItemRepository.findLatestSequence());
                                    salesOrderItem.setOrgId(userSession.getCompany().getCompanyId());
                                    salesOrderItem.setSalesOrderId(salesOrder.getId());
                                    salesOrderItem.setRequestOrderId(requestProductMapping.getRequest().getRequestOrderId());
                                    salesOrderItem.setProductId(productMaster.getProductId());
                                    salesOrderItem.setProductQuantity(productMaster.getQuantity());
                                    salesOrderItem.setProductAmount(productMaster.getAmount());
                                    salesOrderItem.setInsertedBy(userSession.getSub());
                                    salesOrderItem.setInsertedOn(LocalDateTime.now());

                                    salesOrderItemsList.add(salesOrderItem);
                                }
                            } else {
                                var salesOrderUpdateIndex = salesOrderList.size() - 1;
                                var salesOrderToBeUpdated = salesOrderList.get(salesOrderUpdateIndex);
                                salesOrderToBeUpdated.setRequestId(salesOrderToBeUpdated.getRequestId() + ", " + "'" + requestProductMapping.getRequest().getRequestOrderId() + "'");

                                salesOrderList.set(salesOrderUpdateIndex, salesOrderToBeUpdated);

                                requestOrders.add(requestProductMapping.getRequest());

                                for (ProductMaster productMaster : requestProductMapping.getProducts()) {
                                    var salesOrderItem = new SalesOrderItem();
                                    salesOrderItem.setId(salesOrderItemRepository.findLatestSequence());
                                    salesOrderItem.setOrgId(userSession.getCompany().getCompanyId());
                                    salesOrderItem.setSalesOrderId(salesOrderList.get(salesOrderList.size() - 1).getId());
                                    salesOrderItem.setRequestOrderId(requestProductMapping.getRequest().getRequestOrderId());
                                    salesOrderItem.setProductId(productMaster.getProductId());
                                    salesOrderItem.setProductQuantity(productMaster.getQuantity());
                                    salesOrderItem.setProductAmount(productMaster.getAmount());
                                    salesOrderItem.setInsertedBy(userSession.getSub());
                                    salesOrderItem.setInsertedOn(LocalDateTime.now());

                                    salesOrderItemsList.add(salesOrderItem);
                                }
                            }
                        } else {
                            productAmount.totalProductAmount = productAmount.totalProductAmount.subtract(salesOrderRequest.getOrderAmount());
                            var salesOrder = new SalesOrder();
                            var sequence = salesOrderRepository.findLatestSequence();
                            salesOrder.setId(sequence);
                            salesOrder.setSalesOrderId("SO-" + sequence);
                            salesOrder.setSalesOrderNumber("SO-" + sequence);
                            salesOrder.setRequestId("'" + requestProductMapping.getRequest().getRequestOrderId() + "'");
                            salesOrder.setOrgId(userSession.getCompany().getCompanyId());
                            salesOrder.setPktReference(null);
                            salesOrder.setBatchId(batchId);
                            salesOrder.setStatus("P");
                            salesOrder.setSalesOrderType(salesOrderRequest.getOrderType());
                            salesOrder.setInsertedBy(userSession.getSub());
                            salesOrder.setInsertedOn(LocalDateTime.now());

                            salesOrderList.add(salesOrder);

                            requestOrders.add(requestProductMapping.getRequest());

                            for (ProductMaster productMaster : requestProductMapping.getProducts()) {
                                var salesOrderItem = new SalesOrderItem();
                                salesOrderItem.setId(salesOrderItemRepository.findLatestSequence());
                                salesOrderItem.setOrgId(userSession.getCompany().getCompanyId());
                                salesOrderItem.setSalesOrderId(salesOrder.getId());
                                salesOrderItem.setRequestOrderId(requestProductMapping.getRequest().getRequestOrderId());
                                salesOrderItem.setProductId(productMaster.getProductId());
                                salesOrderItem.setProductQuantity(productMaster.getQuantity());
                                salesOrderItem.setProductAmount(productMaster.getAmount());
                                salesOrderItem.setInsertedBy(userSession.getSub());
                                salesOrderItem.setInsertedOn(LocalDateTime.now());

                                salesOrderItemsList.add(salesOrderItem);
                            }
                        }
                    }

                    var response = salesOrderTransactionService.processSalesOrderGeneration(salesOrderList, salesOrderItemsList, requestOrders);
                    if (response.getStatus() == HttpStatus.OK) {
                        log.info("Sales order saved successfully for requestIds: {}", requestProductMappingList);
                    } else {
                        return new Response("Something went wrong!", HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception exception) {
                    log.info("Something went wrong while saving combination sales order. Reason: {}", exception.getMessage());
                    return new Response("Something went wrong!", HttpStatus.BAD_REQUEST);
                }
                break;
            }

            case "item": {
                log.info("Request received for creating a sales order. Order type: {}", salesOrderRequest.getOrderType());

                var batchId = commonUtil.getBatchId(userSession);

                var requestProductMappingList = new ArrayList<RequestProductMapping>();
                for (Long request : salesOrderRequest.getRequestList()) {
                    var requestProductMapping = new RequestProductMapping();
                    var requestOrder = requestOrderRepository.findByRequestOrderIdAndOrgIdAndStatus(request, userSession.getCompany().getCompanyId(), "P");
                    if (requestOrder.isPresent()) {
                        requestProductMapping.setRequest(requestOrder.get());
                        var product = productMasterRepository.findByOrgIdAndProductId(userSession.getCompany().getCompanyId(), requestOrder.get().getProductId());
                        if (product.isPresent()) {
                            if (product.get().getIsCombo().equals("Y")) {
                                var comboProducts = productMasterRepository.findByOrgIdAndParentProductId(userSession.getCompany().getCompanyId(), product.get().getProductId());
                                if (comboProducts.isPresent()) {
                                    if (!comboProducts.get().isEmpty()) {
                                        requestProductMapping.setProducts(comboProducts.get());
                                    } else {
                                        log.info("No parent product id found for productId: {}", product.get().getProductId());
                                        return new Response("No products found for the selected requests in the system!. RequestID: " + request, HttpStatus.BAD_REQUEST);
                                    }
                                } else {
                                    log.info("No parent product id found for productId: {}", product.get().getProductId());
                                    return new Response("No products found for the selected requests in the system!. RequestID: " + request, HttpStatus.BAD_REQUEST);
                                }
                            } else {
                                requestProductMapping.setProducts(List.of(product.get()));
                            }
                            requestProductMappingList.add(requestProductMapping);
                        } else {
                            log.info("No products found for the given input request order: {} and product: {}", request, requestOrder.get().getProductId());
                            return new Response("No products found for the selected requests in the system!. RequestID: " + request, HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        log.info("No request orders found for the given input request order list: {}", salesOrderRequest.getRequestList());
                        return new Response("No request orders found for the selected requests in the system!", HttpStatus.BAD_REQUEST);
                    }
                }

                var salesOrderList = new ArrayList<SalesOrder>();
                var salesOrderItemList = new ArrayList<SalesOrderItem>();
                var requestOrders = new ArrayList<RequestOrder>();

                requestProductMappingList.forEach(requestProductMapping -> requestProductMapping.getProducts().forEach(productMaster -> {
                    var salesOrder = new SalesOrder();
                    var sequence = salesOrderRepository.findLatestSequence();
                    salesOrder.setId(sequence);
                    salesOrder.setSalesOrderId("SO-" + sequence);
                    salesOrder.setSalesOrderNumber("SO-" + sequence);
                    salesOrder.setRequestId("'" + requestProductMapping.getRequest().getRequestOrderId() + "'");
                    salesOrder.setOrgId(userSession.getCompany().getCompanyId());
                    salesOrder.setPktReference(null);
                    salesOrder.setBatchId(batchId);
                    salesOrder.setStatus("P");
                    salesOrder.setSalesOrderType(salesOrderRequest.getOrderType());
                    salesOrder.setInsertedBy(userSession.getSub());
                    salesOrder.setInsertedOn(LocalDateTime.now());

                    salesOrderList.add(salesOrder);

                    requestOrders.add(requestProductMapping.getRequest());

                    var salesOrderItem = new SalesOrderItem();
                    salesOrderItem.setId(salesOrderItemRepository.findLatestSequence());
                    salesOrderItem.setOrgId(userSession.getCompany().getCompanyId());
                    salesOrderItem.setSalesOrderId(salesOrder.getId());
                    salesOrderItem.setRequestOrderId(requestProductMapping.getRequest().getRequestOrderId());
                    salesOrderItem.setProductId(productMaster.getProductId());
                    salesOrderItem.setProductQuantity(productMaster.getQuantity());
                    salesOrderItem.setProductAmount(productMaster.getAmount());
                    salesOrderItem.setInsertedBy(userSession.getSub());
                    salesOrderItem.setInsertedOn(LocalDateTime.now());

                    salesOrderItemList.add(salesOrderItem);
                }));

                var response = salesOrderTransactionService.processSalesOrderGeneration(salesOrderList, salesOrderItemList, requestOrders);
                if (response.getStatus() == HttpStatus.OK) {
                    log.info("Sales order saved successfully for requestIds: {}", requestProductMappingList);
                } else {
                    return new Response("Something went wrong!", HttpStatus.BAD_REQUEST);
                }
                break;
            }

            case "request": {
                log.info("Request received for creating a sales order. Order type: {}", salesOrderRequest.getOrderType());

                var batchId = commonUtil.getBatchId(userSession);

                var requestProductMappingList = new ArrayList<RequestProductMapping>();
                for (Long request : salesOrderRequest.getRequestList()) {
                    var requestProductMapping = new RequestProductMapping();
                    var requestOrder = requestOrderRepository.findByRequestOrderIdAndOrgIdAndStatus(request, userSession.getCompany().getCompanyId(), "P");
                    if (requestOrder.isPresent()) {
                        requestProductMapping.setRequest(requestOrder.get());
                        var product = productMasterRepository.findByOrgIdAndProductId(userSession.getCompany().getCompanyId(), requestOrder.get().getProductId());
                        if (product.isPresent()) {
                            if (product.get().getIsCombo().equals("Y")) {
                                var comboProducts = productMasterRepository.findByOrgIdAndParentProductId(userSession.getCompany().getCompanyId(), product.get().getProductId());
                                if (comboProducts.isPresent()) {
                                    if (!comboProducts.get().isEmpty()) {
                                        requestProductMapping.setProducts(comboProducts.get());
                                    } else {
                                        log.info("No parent product id found for productId: {}", product.get().getProductId());
                                        return new Response("No products found for the selected requests in the system!. RequestID: " + request, HttpStatus.BAD_REQUEST);
                                    }
                                } else {
                                    log.info("No parent product id found for productId: {}", product.get().getProductId());
                                    return new Response("No products found for the selected requests in the system!. RequestID: " + request, HttpStatus.BAD_REQUEST);
                                }
                            } else {
                                requestProductMapping.setProducts(List.of(product.get()));
                            }
                            requestProductMappingList.add(requestProductMapping);
                        } else {
                            log.info("No products found for the given input request order: {} and product: {}", request, requestOrder.get().getProductId());
                            return new Response("No products found for the selected requests in the system!. RequestID: " + request, HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        log.info("No request orders found for the given input request order list: {}", salesOrderRequest.getRequestList());
                        return new Response("No request orders found for the selected requests in the system!", HttpStatus.BAD_REQUEST);
                    }
                }

                var salesOrderList = new ArrayList<SalesOrder>();
                var salesOrderItemList = new ArrayList<SalesOrderItem>();
                var requestOrders = new ArrayList<RequestOrder>();

                requestProductMappingList.forEach(requestProductMapping -> {
                    var salesOrder = new SalesOrder();
                    var sequence = salesOrderRepository.findLatestSequence();
                    salesOrder.setId(sequence);
                    salesOrder.setSalesOrderId("SO-" + sequence);
                    salesOrder.setSalesOrderNumber("SO-" + sequence);
                    salesOrder.setRequestId("'" + requestProductMapping.getRequest().getRequestOrderId() + "'");
                    salesOrder.setOrgId(userSession.getCompany().getCompanyId());
                    salesOrder.setPktReference(null);
                    salesOrder.setBatchId(batchId);
                    salesOrder.setStatus("P");
                    salesOrder.setSalesOrderType(salesOrderRequest.getOrderType());
                    salesOrder.setInsertedBy(userSession.getSub());
                    salesOrder.setInsertedOn(LocalDateTime.now());

                    salesOrderList.add(salesOrder);

                    requestOrders.add(requestProductMapping.getRequest());

                    requestProductMapping.getProducts().forEach(productMaster -> {
                        var salesOrderItem = new SalesOrderItem();
                        salesOrderItem.setId(salesOrderItemRepository.findLatestSequence());
                        salesOrderItem.setOrgId(userSession.getCompany().getCompanyId());
                        salesOrderItem.setSalesOrderId(salesOrder.getId());
                        salesOrderItem.setRequestOrderId(requestProductMapping.getRequest().getRequestOrderId());
                        salesOrderItem.setProductId(productMaster.getProductId());
                        salesOrderItem.setProductQuantity(productMaster.getQuantity());
                        salesOrderItem.setProductAmount(productMaster.getAmount());
                        salesOrderItem.setInsertedBy(userSession.getSub());
                        salesOrderItem.setInsertedOn(LocalDateTime.now());

                        salesOrderItemList.add(salesOrderItem);
                    });
                });

                var response = salesOrderTransactionService.processSalesOrderGeneration(salesOrderList, salesOrderItemList, requestOrders);
                if (response.getStatus() == HttpStatus.OK) {
                    log.info("Sales order saved successfully for requestIds: {}", requestProductMappingList);
                } else {
                    return new Response("Something went wrong!", HttpStatus.BAD_REQUEST);
                }
                break;
            }

            default: {
                return new Response("Please enter a valid order type!", HttpStatus.BAD_REQUEST);
            }
        }
        return new Response("Transaction completed successfully!", HttpStatus.OK);
    }

    @Override
    public Response uploadSalesOrderFile(MultipartFile file, HttpServletResponse httpServletResponse) {
        return null;
    }

    @Override
    public Response downloadSalesOrderFile(Long salesOrderId, HttpServletResponse httpServletResponse) {
        var userSession = userCredentialService.getUserSession();
        try {
            var salesOrder = salesOrderRepository.findByOrgIdAndId(userSession.getCompany().getCompanyId(), salesOrderId);
            var salesOrderItemDtoList = new ArrayList<SalesOrderItemDto>();
            if (salesOrder.isPresent()) {
                var salesOrderItems = salesOrderItemRepository.findByOrgIdAndSalesOrderIdIn(userSession.getCompany().getCompanyId(), List.of(salesOrder.get().getId()));
                if (salesOrderItems.isPresent()) {
                    for (SalesOrderItem salesOrderItem : salesOrderItems.get()) {
                        var requestOrder = requestOrderRepository.findByRequestOrderIdAndOrgId(salesOrderItem.getRequestOrderId(), userSession.getCompany().getCompanyId());
                        if (requestOrder.isPresent()) {
                            var productMaster = productMasterRepository.findByOrgIdAndProductId(userSession.getCompany().getCompanyId(), salesOrderItem.getProductId());
                            if (productMaster.isPresent()) {
                                var salesOrderItemDto = new SalesOrderItemDto();
                                salesOrderItemDto.setSalesOrderId(salesOrder.get().getSalesOrderId());
                                salesOrderItemDto.setOrderDate(DateTimeUtil.dateTimeToString(salesOrder.get().getInsertedOn(), DateTimeUtil.DDMMYYYY));
                                salesOrderItemDto.setSalesOrderNumber(salesOrder.get().getSalesOrderNumber());
                                salesOrderItemDto.setCustomerName(String.valueOf(requestOrder.get().getClientId()));
                                salesOrderItemDto.setPlaceOfSupply(requestOrder.get().getBranchAddress());
                                salesOrderItemDto.setItemName(productMaster.get().getProductName());
                                salesOrderItemDto.setSku("SKU_1");
                                salesOrderItemDto.setQuantityOrdered(0);
                                salesOrderItemDto.setUsageUnit("USAGE UNIT 1");
                                salesOrderItemDto.setWarehouseName("WAREHOUSE NANKO BASEMENT");
                                salesOrderItemDto.setItemPrice(salesOrderItem.getProductAmount());
                                salesOrderItemDto.setHsnOrSac("1220001");
                                salesOrderItemDto.setSupplyType("SUPPLY TYPE 1");
                                salesOrderItemDto.setItemTax(new BigDecimal(0));
                                salesOrderItemDto.setItemTaxPercent(new BigDecimal("0"));
                                salesOrderItemDto.setItemTaxAmount(new BigDecimal("0"));
                                salesOrderItemDto.setItemTaxType("NO TAX");
                                salesOrderItemDto.setItemType("GOODS");
                                salesOrderItemDto.setItemTotal(salesOrderItem.getProductAmount());
                                salesOrderItemDto.setSubTotal(salesOrderItem.getProductAmount());
                                salesOrderItemDto.setTotal(salesOrderItem.getProductAmount());
                                salesOrderItemDto.setPaymentTermsLabel("BLACK LABEL");
                                salesOrderItemDto.setDeliveryMethod("COD");
                                salesOrderItemDto.setBillingCity(requestOrder.get().getBranchAddress());
                                salesOrderItemDto.setBillingState(requestOrder.get().getBranchAddress());
                                salesOrderItemDto.setBillingCountry("");
                                salesOrderItemDto.setShippingAddress("");
                                salesOrderItemDto.setShippingCity("");
                                salesOrderItemDto.setShippingState("");
                                salesOrderItemDto.setShippingCountry("");
                                salesOrderItemDto.setCfBranchName(requestOrder.get().getBranchCode());
                                salesOrderItemDto.setCfClientId(String.valueOf(requestOrder.get().getClientId()));
                                salesOrderItemDto.setCfCustomerSupport(requestOrder.get().getCustomerSupportNumber());

                                salesOrderItemDtoList.add(salesOrderItemDto);

                            } else {
                                log.info("Product does not exists for sales orderItemID: {}", salesOrderItem.getId());
                                return new Response("Sales order items does not exists!", HttpStatus.NOT_FOUND);
                            }
                        } else {
                            log.info("Request order does not exists for sales orderID: {}, sales order item ID: {}", salesOrder.get().getSalesOrderId(), salesOrderItem.getId());
                            return new Response("Sales order items does not exists!", HttpStatus.NOT_FOUND);
                        }
                    }

                    downloadSalesOrderExcel(salesOrderItemDtoList, httpServletResponse);
                    return null;

                } else {
                    log.info("Sales order items does not exists for sales orderID: {}", salesOrder.get().getSalesOrderId());
                    return new Response("Sales order items does not exists!", HttpStatus.NOT_FOUND);
                }
            } else {
                log.info("Sales order does not exists for ID: {}", salesOrderId);
                return new Response("Sales order does not exists!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            log.info("Exception occurred while fetching sales order item data. Reason: {}", exception.getMessage());
            return new Response("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Response getSalesOrderItems(FilterRequest filterRequest) {
        var userSession = userCredentialService.getUserSession();
        if (!StringUtils.hasText(String.valueOf(filterRequest.getPage()))
                || !StringUtils.hasText(String.valueOf(filterRequest.getSize()))
                || !StringUtils.hasText(filterRequest.getSalesOrderId())) {
            return new Response("Invalid Request", HttpStatus.BAD_REQUEST);
        }
        try {
            var salesOrder = salesOrderRepository.findByOrgIdAndId(userSession.getCompany().getCompanyId(), Long.valueOf(filterRequest.getSalesOrderId()));
            var salesOrderItemDtoList = new ArrayList<SalesOrderItemDto>();
            if (salesOrder.isPresent()) {
                var totalSalesOrderItemsCount = salesOrderItemRepository.findTotalSalesItemCount(userSession.getCompany().getCompanyId(), Long.valueOf(filterRequest.getSalesOrderId()));
                log.info("item count: {}", totalSalesOrderItemsCount);
                var salesOrderItems = salesOrderItemRepository.findAllByOrgIdAndSalesOrderId(userSession.getCompany().getCompanyId(), salesOrder.get().getId(), PageRequest.of(filterRequest.getPage(), filterRequest.getSize())).getContent();
                if (!salesOrderItems.isEmpty()) {
                    for (SalesOrderItem salesOrderItem : salesOrderItems) {
                        var requestOrder = requestOrderRepository.findByRequestOrderIdAndOrgId(salesOrderItem.getRequestOrderId(), userSession.getCompany().getCompanyId());
                        if (requestOrder.isPresent()) {
                            var productMaster = productMasterRepository.findByOrgIdAndProductId(userSession.getCompany().getCompanyId(), salesOrderItem.getProductId());
                            if (productMaster.isPresent()) {
                                var salesOrderItemDto = new SalesOrderItemDto();
                                salesOrderItemDto.setSalesOrderId(salesOrder.get().getSalesOrderId());
                                salesOrderItemDto.setOrderDate(DateTimeUtil.dateTimeToString(salesOrder.get().getInsertedOn(), DateTimeUtil.DDMMYYYY));
                                salesOrderItemDto.setSalesOrderNumber(salesOrder.get().getSalesOrderNumber());
                                salesOrderItemDto.setCustomerName(String.valueOf(requestOrder.get().getClientId()));
                                salesOrderItemDto.setPlaceOfSupply(requestOrder.get().getBranchAddress());
                                salesOrderItemDto.setItemName(productMaster.get().getProductName());
                                salesOrderItemDto.setSku("SKU_1");
                                salesOrderItemDto.setQuantityOrdered(0);
                                salesOrderItemDto.setUsageUnit("USAGE UNIT 1");
                                salesOrderItemDto.setWarehouseName("WAREHOUSE NANKO BASEMENT");
                                salesOrderItemDto.setItemPrice(salesOrderItem.getProductAmount());
                                salesOrderItemDto.setHsnOrSac("1220001");
                                salesOrderItemDto.setSupplyType("SUPPLY TYPE 1");
                                salesOrderItemDto.setItemTax(new BigDecimal(0));
                                salesOrderItemDto.setItemTaxPercent(new BigDecimal("0"));
                                salesOrderItemDto.setItemTaxAmount(new BigDecimal("0"));
                                salesOrderItemDto.setItemTaxType("NO TAX");
                                salesOrderItemDto.setItemType("GOODS");
                                salesOrderItemDto.setItemTotal(salesOrderItem.getProductAmount());
                                salesOrderItemDto.setSubTotal(salesOrderItem.getProductAmount());
                                salesOrderItemDto.setTotal(salesOrderItem.getProductAmount());
                                salesOrderItemDto.setPaymentTermsLabel("BLACK LABEL");
                                salesOrderItemDto.setDeliveryMethod("COD");
                                salesOrderItemDto.setBillingCity(requestOrder.get().getBranchAddress());
                                salesOrderItemDto.setBillingState(requestOrder.get().getBranchAddress());
                                salesOrderItemDto.setBillingCountry("");
                                salesOrderItemDto.setShippingAddress("");
                                salesOrderItemDto.setShippingCity("");
                                salesOrderItemDto.setShippingState("");
                                salesOrderItemDto.setShippingCountry("");
                                salesOrderItemDto.setCfBranchName(requestOrder.get().getBranchCode());
                                salesOrderItemDto.setCfClientId(String.valueOf(requestOrder.get().getClientId()));
                                salesOrderItemDto.setCfCustomerSupport(requestOrder.get().getCustomerSupportNumber());

                                salesOrderItemDtoList.add(salesOrderItemDto);

                            } else {
                                log.info("Product does not exists for sales orderItemID: {}", salesOrderItem.getId());
                                return new Response("Sales order items does not exists!", HttpStatus.NOT_FOUND);
                            }
                        } else {
                            log.info("Request order does not exists for sales orderID: {}, sales order item ID: {}", salesOrder.get().getSalesOrderId(), salesOrderItem.getId());
                            return new Response("Sales order items does not exists!", HttpStatus.NOT_FOUND);
                        }
                    }

                    log.info("Successfully created sales order item dto");
                    return new Response("Transaction completed successfully", salesOrderItemDtoList, totalSalesOrderItemsCount, HttpStatus.OK);

                } else {
                    log.info("Sales order items does not exists for sales orderID: {}", salesOrder.get().getSalesOrderId());
                    return new Response("Sales order items does not exists!", HttpStatus.NOT_FOUND);
                }
            } else {
                log.info("Sales order does not exists for ID: {}", filterRequest.getSalesOrderId());
                return new Response("Sales order does not exists!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            log.info("Exception occurred while fetching sales order item data. Reason: {}", exception.getMessage());
            return new Response("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    private void downloadSalesOrderExcel(List<SalesOrderItemDto> data, HttpServletResponse httpServletResponse) {
        try (var workbook = new HSSFWorkbook()) {
            var map = excelGeneratorUtil.populateHeaderAndName(SALES_ORDER_HEADER, "sales_order_data.xls");
            map.put("RESULTS", salesOrderAssembler.prepareSalesOrderData(data));
            excelGeneratorUtil.buildExcelDocument(map, workbook);
            excelGeneratorUtil.downloadDocument(httpServletResponse, map, workbook);
        } catch (Exception exception) {
            log.error("Exception occurs while downloading Sales Order Error Excel {}", exception.getMessage());
        }
    }

}
