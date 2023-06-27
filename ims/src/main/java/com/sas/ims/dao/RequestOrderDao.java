package com.sas.ims.dao;

import com.sas.ims.entity.RequestOrder;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.request.FilterRequest;
import com.sas.tokenlib.utils.UserSession;

import java.util.List;

public interface RequestOrderDao {
    List<RequestOrder> findAllRequestOrder(FilterRequest filterRequest, UserSession userSession) throws BadRequestException;
    Long findAllRequestOrderCount(FilterRequest filterRequest, UserSession userSession) throws BadRequestException;
}
