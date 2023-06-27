package com.sas.ims.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SalesOrderRequest {
    private List<Long> requestList;
    private String orderType;
    private BigDecimal orderAmount;
}