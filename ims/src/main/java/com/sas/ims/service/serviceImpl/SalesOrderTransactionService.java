package com.sas.ims.service.serviceImpl;

import com.sas.ims.entity.RequestOrder;
import com.sas.ims.entity.SalesOrder;
import com.sas.ims.entity.SalesOrderItem;
import com.sas.ims.repository.RequestOrderRepository;
import com.sas.ims.repository.SalesOrderItemRepository;
import com.sas.ims.repository.SalesOrderRepository;
import com.sas.tokenlib.response.Response;
import com.sas.tokenlib.utils.UserSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SalesOrderTransactionService {
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final RequestOrderRepository requestOrderRepository;

    @Transactional(rollbackFor = Exception.class)
    public Response processSalesOrderGeneration(List<SalesOrder> salesOrders, List<SalesOrderItem> salesOrderItems, List<RequestOrder> requestOrders) {
        try {
            salesOrders.forEach(salesOrderRepository::save);
            salesOrderItems.forEach(salesOrderItemRepository::save);
            requestOrders.forEach(requestOrder -> requestOrderRepository.updateRequestOrderStatusToComplete(requestOrder.getOrgId(), requestOrder.getRequestOrderId()));
            return new Response("Transaction completed successfully", HttpStatus.OK);
        } catch (Exception exception) {
            log.info("Exception occurred while saving sales orders and sales items. Reason: {}", exception.getMessage());
            return new Response("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

}
