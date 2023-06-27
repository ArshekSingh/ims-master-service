package com.sas.ims.service.serviceImpl;

import com.sas.ims.entity.RequestOrder;
import com.sas.ims.repository.RequestOrderRepository;
import com.sas.ims.response.RequestOrderXlsResponse;
import com.sas.tokenlib.utils.UserSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RequestOrderTransactionService {
    private final RequestOrderRepository requestOrderRepository;

    @Transactional(rollbackFor = Exception.class)
    public void processRequestOrderGeneration(UserSession userSession, List<RequestOrderXlsResponse> xlsResponseList) {
        xlsResponseList.forEach(xlsResponse -> {
            log.info("Saving request order for loanId: {}, clientId: {}", xlsResponse.getLoanId(), xlsResponse.getClientId());
            RequestOrder requestOrder = new RequestOrder();
            requestOrder.setOrgId(userSession.getCompany().getCompanyId());
            requestOrder.setLoanId(xlsResponse.getLoanId());
            requestOrder.setLoanCode(xlsResponse.getLoanCode());
            requestOrder.setClientId(xlsResponse.getClientId());
            requestOrder.setProductId(xlsResponse.getProductId());
            requestOrder.setProductCode(xlsResponse.getProductCode());
            requestOrder.setBranchId(xlsResponse.getBranchId());
            requestOrder.setBranchCode(xlsResponse.getBranchCode());
            requestOrder.setBranchAddress(xlsResponse.getBranchAddress());
            requestOrder.setCustomerSupportNumber(xlsResponse.getCustomerSupportNumber());
            requestOrder.setRequestOrderDate(LocalDateTime.now());
            requestOrder.setStatus("P");
            requestOrder.setBatchId(xlsResponse.getBatchId());
            requestOrder.setInsertedBy(userSession.getSub());
            requestOrder.setInsertedOn(LocalDateTime.now());
            requestOrderRepository.save(requestOrder);
        });
    }

}
