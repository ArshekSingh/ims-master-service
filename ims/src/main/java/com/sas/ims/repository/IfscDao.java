package com.sas.ims.repository;


import com.sas.ims.entity.IfscMaster;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.request.FilterRequest;

import java.util.List;

public interface IfscDao {
    List<IfscMaster> findAllIfscCodeMaster(FilterRequest filterRequest) throws BadRequestException;

    Long findAllIfscCodeCount(FilterRequest filterRequest) throws BadRequestException;
}