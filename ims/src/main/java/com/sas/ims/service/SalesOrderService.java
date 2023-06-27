package com.sas.ims.service;

import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.request.SalesOrderRequest;
import com.sas.tokenlib.response.Response;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface SalesOrderService {
    Response getSalesOrder(Long salesOrderId) throws ObjectNotFoundException;

    Response getSalesOrderList(FilterRequest filterRequest) throws BadRequestException;

    Response createSalesOrder(SalesOrderRequest salesOrderRequest) throws ObjectNotFoundException, BadRequestException;

    Response uploadSalesOrderFile(MultipartFile file, HttpServletResponse httpServletResponse) throws BadRequestException, IOException;

    Response downloadSalesOrderFile(Long salesOrderId, HttpServletResponse httpServletResponse) throws BadRequestException, IOException;

    Response getSalesOrderItems(FilterRequest filterRequest) throws BadRequestException, ObjectNotFoundException;
}
