package com.sas.ims.service;

import com.sas.ims.dto.ProductsToVendorMappingDto;
import com.sas.ims.dto.VendorDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.tokenlib.response.Response;

public interface VendorService {
    Response getVendorById(Long vendorId);

    Response getVendorList(Integer page, Integer size);

    Response saveVendor(VendorDto vendorDto);

    Response updateVendor(VendorDto vendorDto) throws BadRequestException;

    Response removeVendor(Long vendorId);

    Response getProductsAssignedOrAvailableToVendor(Long vendorId);

    Response assignProductsToVendor(ProductsToVendorMappingDto productsToVendorMappingDto);
}
