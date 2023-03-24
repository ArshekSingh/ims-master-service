package com.sts.ims.service;

import com.sts.ims.dto.VendorDto;
import com.sts.ims.response.Response;

public interface VendorService {
    Response getVendorById(Long vendorId);

    Response getVendorList(Integer page, Integer size);

    Response saveVendor(VendorDto vendorDto);

    Response updateVendor(VendorDto vendorDto);

    Response removeVendor(VendorDto vendorDto);
}
