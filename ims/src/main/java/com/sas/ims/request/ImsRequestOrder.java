package com.sas.ims.request;

import lombok.Data;

@Data
public class ImsRequestOrder {
    private Long xSellId;
    private Long orgId;
    private Long loanId;
    private String batchId;
    private String loanCode;
    private Long clientId;
    private Integer productId;
    private String productCode;
    private Integer branchId;
    private String branchCode;
    private String branchAddress;
    private String customerSupportNumber;
}
