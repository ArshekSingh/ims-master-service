package com.sas.ims.dao;

import com.sas.ims.entity.SalesOrder;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.request.FilterRequest;
import com.sas.tokenlib.utils.UserSession;

import java.util.List;

public interface SalesOrderDao {
    List<SalesOrder> findAllSalesOrder(FilterRequest filterRequest, UserSession userSession) throws BadRequestException;
    Long findAllSalesOrderCount(FilterRequest filterRequest, UserSession userSession) throws BadRequestException;
}
