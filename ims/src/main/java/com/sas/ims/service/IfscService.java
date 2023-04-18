package com.sas.ims.service;

import com.sas.ims.dto.IfscMasterDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.FilterRequest;
import com.sas.tokenlib.response.Response;
import org.springframework.stereotype.Service;

@Service
public interface IfscService {

    Response getIfscDetails(String ifscCode) throws ObjectNotFoundException;

    Response getIfscCodeList(String ifscCode);

    Response addIfscCode(IfscMasterDto ifscMasterDto);

    Response updateIfscCode(IfscMasterDto ifscMasterDto) throws ObjectNotFoundException;

    Response getIfscCodeDetails(String ifscCode) throws ObjectNotFoundException;

    Response getIfscCodeDetailsList(FilterRequest filterRequest) throws BadRequestException;
}