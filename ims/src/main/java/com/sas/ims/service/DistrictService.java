package com.sas.ims.service;

import com.sas.ims.dto.DistrictMasterDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.FilterRequest;
import com.sas.tokenlib.response.Response;

public interface DistrictService {

    Response getDistrictDetails(Integer districtId) throws ObjectNotFoundException;

    Response getDistrictDetailList(FilterRequest filterRequest) throws BadRequestException;

    Response addDistrict(DistrictMasterDto districtMasterDto);

    Response updateDistrict(DistrictMasterDto districtMasterDto) throws ObjectNotFoundException;
}