package com.sas.ims.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import com.sas.ims.assembler.RequestOrderAssembler;
import com.sas.ims.config.SqsProperties;
import com.sas.ims.constant.Constant;
import com.sas.ims.dao.RequestOrderDao;
import com.sas.ims.dto.RequestOrderDto;
import com.sas.ims.entity.RequestOrder;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.repository.ProductMasterRepository;
import com.sas.ims.repository.RequestOrderRepository;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.request.RequestOrderUpdateMessage;
import com.sas.ims.request.RequestOrderXlsRequest;
import com.sas.ims.response.RequestOrderXlsResponse;
import com.sas.ims.service.RequestOrderService;
import com.sas.ims.utils.DateTimeUtil;
import com.sas.ims.utils.ExcelGeneratorUtil;
import com.sas.ims.utils.ObjectMapperUtil;
import com.sas.tokenlib.response.Response;
import com.sas.tokenlib.service.UserCredentialService;
import com.sas.tokenlib.utils.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class RequestOrderServiceImpl implements RequestOrderService, Constant {
    private final CommonUtil commonUtil;
    private final RequestOrderRepository requestOrderRepository;
    private final UserCredentialService userCredentialService;
    private final RequestOrderDao requestOrderDao;
    private final ExcelGeneratorUtil excelGeneratorUtil;
    private final RequestOrderAssembler requestOrderAssembler;
    private final RequestOrderTransactionService requestOrderTransactionService;
    private final ProductMasterRepository productMasterRepository;
    private final SqsMessagePublisher sqsMessagePublisher;
    private final ObjectMapper objectMapper;
    private final SqsProperties sqsProperties;

    @Override
    public Response getRequestOrder(Long requestOrderId) throws ObjectNotFoundException {
        var userSession = userCredentialService.getUserSession();
        var requestOrder = requestOrderRepository.findByRequestOrderIdAndOrgId(requestOrderId, userSession.getCompany().getCompanyId()).orElseThrow(() -> new ObjectNotFoundException("Invalid Request Order Id", HttpStatus.NOT_FOUND));
        var productMaster = productMasterRepository.findByOrgIdAndProductId(userSession.getCompany().getCompanyId(), requestOrder.getProductId()).orElseThrow(() -> new ObjectNotFoundException("Invalid product tagged with the order", HttpStatus.NOT_FOUND));
        var requestOrderDto = new RequestOrderDto();
        ObjectMapperUtil.map(requestOrder, requestOrderDto);
        requestOrderDto.setProductAmount(productMaster.getAmount());
        requestOrderDto.setProductName(productMaster.getProductName());
        return new Response(SUCCESS, requestOrderDto, HttpStatus.OK);
    }

    @Override
    public Response getRequestOrderList(FilterRequest filterRequest) throws BadRequestException {
        var userSession = userCredentialService.getUserSession();
        if (!StringUtils.hasText(String.valueOf(filterRequest.getPage()))
                || !StringUtils.hasText(String.valueOf(filterRequest.getSize()))) {
            throw new BadRequestException("Invalid Request", HttpStatus.BAD_REQUEST);
        }
        var requestOrderList = requestOrderDao.findAllRequestOrder(filterRequest, userSession);
        var totalCount = requestOrderDao.findAllRequestOrderCount(filterRequest, userSession);
        List<RequestOrderDto> requestOrderDtoList = new ArrayList<>();
        for (RequestOrder requestOrder : requestOrderList) {
            RequestOrderDto requestOrderDto = new RequestOrderDto();
            ObjectMapperUtil.map(requestOrder, requestOrderDto);
            requestOrderDto.setBatchId(requestOrder.getBatchId());
            requestOrderDto.setRequestOrderDate(DateTimeUtil.dateTimeToString(requestOrder.getRequestOrderDate(), DateTimeUtil.DDMMYYYY));
            var productMaster = productMasterRepository.findByOrgIdAndProductId(userSession.getCompany().getCompanyId(), requestOrder.getProductId());
            if (productMaster.isPresent()) {
                requestOrderDto.setProductName(productMaster.get().getProductName());
                requestOrderDto.setProductAmount(productMaster.get().getAmount());
            }
            requestOrderDtoList.add(requestOrderDto);
        }
        return new Response(SUCCESS, requestOrderDtoList, totalCount, HttpStatus.OK);
    }

    @Override
    public Response addRequestOrder(RequestOrderDto requestOrderDto) {
        var userSession = userCredentialService.getUserSession();
        if (requestOrderDto.getLoanId() == null)
            return new Response("Loan Id is missing", HttpStatus.BAD_REQUEST);
        if (!StringUtils.hasText(requestOrderDto.getLoanCode()))
            return new Response("Loan Code is missing. Invalid request", HttpStatus.BAD_REQUEST);
        if (!StringUtils.hasText(requestOrderDto.getProductCode()))
            return new Response("Product Code is missing. Invalid request", HttpStatus.BAD_REQUEST);
        if (!StringUtils.hasText(requestOrderDto.getBranchCode()))
            return new Response("Branch Code is missing. Invalid request", HttpStatus.BAD_REQUEST);
        if (!StringUtils.hasText(requestOrderDto.getBranchAddress()))
            return new Response("Branch Address is missing. Invalid request", HttpStatus.BAD_REQUEST);
        if (!StringUtils.hasText(requestOrderDto.getCustomerSupportNumber()))
            return new Response("Customer support number is missing. Invalid request", HttpStatus.BAD_REQUEST);
        if (requestOrderDto.getClientId() == null)
            return new Response("Client Id is missing", HttpStatus.BAD_REQUEST);
        if (requestOrderDto.getProductId() == null)
            return new Response("Product Id is missing", HttpStatus.BAD_REQUEST);
        if (requestOrderDto.getBranchId() == null) return new Response("Branch Id is missing", HttpStatus.BAD_REQUEST);

        try {
            var requestOrderExisting = requestOrderRepository.findByLoanIdAndOrgId(requestOrderDto.getLoanId(), userSession.getCompany().getCompanyId());
            if (requestOrderExisting.isPresent()) {
                return new Response("Request already exists!", HttpStatus.BAD_REQUEST);
            } else {
                var requestOrder = new RequestOrder();
                requestOrder.setOrgId(requestOrder.getOrgId());
                requestOrder.setLoanId(requestOrderDto.getLoanId());
                requestOrder.setLoanCode(requestOrderDto.getLoanCode());
                requestOrder.setClientId(requestOrderDto.getClientId());
                requestOrder.setProductId(requestOrderDto.getProductId());
                requestOrder.setProductCode(requestOrderDto.getProductCode());
                requestOrder.setBranchId(requestOrderDto.getBranchId());
                requestOrder.setBranchCode(requestOrderDto.getBranchCode());
                requestOrder.setBranchAddress(requestOrderDto.getBranchAddress());
                requestOrder.setCustomerSupportNumber(requestOrderDto.getCustomerSupportNumber());
                requestOrder.setRequestOrderDate(LocalDateTime.now());
                requestOrder.setStatus("P");
                requestOrder.setInsertedBy(userSession.getSub());
                requestOrder.setInsertedOn(LocalDateTime.now());
                requestOrder = requestOrderRepository.save(requestOrder);

                //Send message to queue in case of successful request order generation
                var updateMessage = new RequestOrderUpdateMessage();
                updateMessage.setStatus(1L);
                updateMessage.setLoanId(requestOrder.getLoanId());

                sqsMessagePublisher.send(objectMapper.writeValueAsString(updateMessage), sqsProperties.getRequirementUpdateUrl());

                log.info("Request update detail sent to sqs queue. LoanId: {}", requestOrder.getLoanId());
            }

        } catch (Exception exception) {
            log.info("Exception occurred while saving request order from xls file. Reason: {}", exception.getMessage());
        }
        return new Response("Transaction completed successfully!", HttpStatus.OK);
    }

    @Override
    public Response uploadRequestOrderFile(MultipartFile file, HttpServletResponse httpServletResponse) throws BadRequestException {
        var userSession = userCredentialService.getUserSession();

        //Checking file format
        if (!commonUtil.checkFileType(file)) {
            log.error("Invalid file type");
            throw new BadRequestException("Invalid file type. Only Xls format accepted", HttpStatus.BAD_REQUEST);
        }

        //Reading Excel Data
        try {
            var options = PoijiOptions.PoijiOptionsBuilder.settings().build();
            var xlsRequestList = Poiji.fromExcel(file.getInputStream(), PoijiExcelType.XLS, RequestOrderXlsRequest.class, options);

            var batchId = commonUtil.getBatchId(userSession);

            var error = new Object() {
                boolean isError = false;
            };

            //ValidateRequest
            var xlsResponseList = new ArrayList<RequestOrderXlsResponse>();
            xlsRequestList.forEach(xlsRequest -> {
                var requestOrderXlsResponse = new RequestOrderXlsResponse();
                requestOrderXlsResponse.setLoanId(xlsRequest.getLoanId());
                requestOrderXlsResponse.setLoanCode(xlsRequest.getLoanCode());
                requestOrderXlsResponse.setClientId(xlsRequest.getClientId());
                requestOrderXlsResponse.setProductId(xlsRequest.getProductId());
                requestOrderXlsResponse.setProductCode(xlsRequest.getProductCode());
                requestOrderXlsResponse.setBranchId(xlsRequest.getBranchId());
                requestOrderXlsResponse.setBranchCode(xlsRequest.getBranchCode());
                requestOrderXlsResponse.setBranchAddress(xlsRequest.getBranchAddress());
                requestOrderXlsResponse.setCustomerSupportNumber(xlsRequest.getCustomerSupportNumber());
                requestOrderXlsResponse.setBatchId(batchId);

                var isError = false;
                var errorMessage = "";

                if (xlsRequest.getLoanId() == null) {
                    isError = true;
                    errorMessage = errorMessage + "Loan Id is empty, ";
                }
                if (!StringUtils.hasText(xlsRequest.getLoanCode())) {
                    isError = true;
                    errorMessage = errorMessage + "Loan Code is empty, ";
                }
                if (xlsRequest.getProductId() == null) {
                    isError = true;
                    errorMessage = errorMessage + "ProductId is empty, ";
                }
                if (!StringUtils.hasText(xlsRequest.getProductCode())) {
                    isError = true;
                    errorMessage = errorMessage + "Product code is empty, ";
                }
                if (!StringUtils.hasText(xlsRequest.getBranchCode())) {
                    isError = true;
                    errorMessage = errorMessage + "Branch code is empty, ";
                }
                if (!StringUtils.hasText(xlsRequest.getBranchAddress())) {
                    isError = true;
                    errorMessage = errorMessage + "Branch Address is empty, ";
                }
                if (xlsRequest.getBranchId() == null) {
                    isError = true;
                    errorMessage = errorMessage + "Branch Id is empty, ";
                }
                if (!StringUtils.hasText(xlsRequest.getCustomerSupportNumber())) {
                    isError = true;
                    errorMessage = errorMessage + "Customer support number is empty, ";
                }

                if (isError) {
                    var requestOrder = requestOrderRepository.findByLoanIdAndOrgId(xlsRequest.getLoanId(), userSession.getCompany().getCompanyId());
                    if (requestOrder.isPresent()) {
                        error.isError = true;
                        log.info("Request order already exists for this entry. LoanId: {}, ClientId: {}", xlsRequest.getLoanId(), xlsRequest.getClientId());
                        requestOrderXlsResponse.setErrorMessage("Record already exists");
                    }
                } else {
                    log.info("Request order parameter missing for this entry. LoanId: {}, ClientId: {}", xlsRequest.getLoanId(), xlsRequest.getClientId());
                    error.isError = true;
                    requestOrderXlsResponse.setErrorMessage(errorMessage);
                }
                xlsResponseList.add(requestOrderXlsResponse);
            });

            if (error.isError) {
                downloadRequestOrderFile(xlsResponseList, httpServletResponse, "request_order_error.xls", true);
            } else {
                try {
                    requestOrderTransactionService.processRequestOrderGeneration(userSession, xlsResponseList);
                    downloadRequestOrderFile(xlsResponseList, httpServletResponse, "request_order_success.xls", false);
                } catch (Exception exception) {
                    log.info("Exception occurred while saving request order from xls file. Reason: {}", exception.getMessage());
                }
            }
        } catch (Exception exception) {
            log.error("Exception occurred while saving request order from xls file. Reason: {}", exception.getMessage());
        }
        return null;
    }

    private void downloadRequestOrderFile(List<RequestOrderXlsResponse> data, HttpServletResponse httpServletResponse, String fileName, boolean isError) {
        try (var workbook = new HSSFWorkbook()) {
            var map = excelGeneratorUtil.populateHeaderAndName(REQUEST_ORDER_ERROR_HEADER, fileName);
            map.put("RESULTS", requestOrderAssembler.prepareRequestOrderErrorData(data));
            if (isError) {
                httpServletResponse.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            } else {
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            }
            excelGeneratorUtil.buildExcelDocument(map, workbook);
            excelGeneratorUtil.downloadDocument(httpServletResponse, map, workbook);
        } catch (Exception exception) {
            log.error("Exception occurs while downloading Request Order Error Excel {}", exception.getMessage());
        }
    }

}
