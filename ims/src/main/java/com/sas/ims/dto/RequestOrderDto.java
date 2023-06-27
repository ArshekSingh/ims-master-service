package com.sas.ims.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestOrderDto {
    private Long xSellId;
    private Long orgId;
    private Long loanId;
    private String batchId;
    private String loanCode;
    private Long clientId;
    private Long productId;
    private String productCode;
    private Integer branchId;
    private String branchCode;
    private String branchAddress;
    private String customerSupportNumber;
    private String requestOrderDate;
    private String productName;
    private BigDecimal productAmount;
    private Long requestOrderId;
    private String status;
}
