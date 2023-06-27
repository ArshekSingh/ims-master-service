package com.sas.ims.service;

import com.sas.ims.dto.RequestOrderDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.FilterRequest;
import com.sas.tokenlib.response.Response;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface RequestOrderService {
    Response getRequestOrder(Long requestOrderId) throws ObjectNotFoundException;

    Response getRequestOrderList(FilterRequest filterRequest) throws BadRequestException;

    Response addRequestOrder(RequestOrderDto requestOrderDto) throws ObjectNotFoundException, BadRequestException;

    Response uploadRequestOrderFile(MultipartFile file, HttpServletResponse httpServletResponse) throws BadRequestException, IOException;
}
