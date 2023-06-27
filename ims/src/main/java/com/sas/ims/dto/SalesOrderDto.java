package com.sas.ims.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesOrderDto {
    private Long id;
    private String salesOrderId;
    private String salesOrderNumber;
    private String batchId;
    private String createdBy;
    private String createdOn;
    private String status;
    private BigDecimal orderAmount;
    private String orderType;
}
