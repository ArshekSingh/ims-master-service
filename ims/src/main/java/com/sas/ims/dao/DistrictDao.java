package com.sas.ims.dao;


import com.sas.ims.entity.DistrictMaster;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.request.FilterRequest;

import java.util.List;

public interface DistrictDao {
    List<DistrictMaster> findAllDistrictMaster(FilterRequest filterRequest) throws BadRequestException;

    Long findAllDistrictMasterCount(FilterRequest request) throws BadRequestException;
}