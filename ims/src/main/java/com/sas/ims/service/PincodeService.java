package com.sas.ims.service;


import com.sas.ims.dto.PincodeMasterDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.FilterRequest;
import com.sas.tokenlib.response.Response;

public interface PincodeService {

    Response getPincodeList(Integer pincode);

    Response addPincode(PincodeMasterDto pincodeMasterDto);

    Response updatePincode(PincodeMasterDto pincodeMasterDto) throws ObjectNotFoundException;

    Response getPincodeDetails(Integer pincode) throws ObjectNotFoundException;

    Response getPincodeDetailsList(FilterRequest filterRequest) throws BadRequestException;
}