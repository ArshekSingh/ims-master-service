package com.sas.ims.service;

import com.sas.ims.dto.AreasToVendorMappingDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.InternalServerErrorException;
import com.sas.tokenlib.response.Response;

public interface VendorAreaMappingService {

    Response getAreasAssignedOrAvailableToVendor(Long vendorId);

    Response assignAreasToVendor(AreasToVendorMappingDto areasToVendorMappingDto);

    Response getVendorsList() throws BadRequestException, InternalServerErrorException;
}
