package com.sas.ims.dao;


import com.sas.ims.entity.PincodeMaster;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.request.FilterRequest;

import java.util.List;

public interface PincodeDao {
    List<PincodeMaster> findAllPincodeMaster(FilterRequest filterRequest) throws BadRequestException;

    Long findAllPincodeMasterCount(FilterRequest filterRequest) throws BadRequestException;
}