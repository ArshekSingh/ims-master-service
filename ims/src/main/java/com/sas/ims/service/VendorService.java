package com.sas.ims.service;

import com.sas.ims.dto.VendorDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.response.Response;

public interface VendorService {
    Response getVendorById(Long vendorId);

    Response getVendorList(Integer page, Integer size);

    Response saveVendor(VendorDto vendorDto);

    Response updateVendor(VendorDto vendorDto) throws BadRequestException;

    Response removeVendor(VendorDto vendorDto);
}
