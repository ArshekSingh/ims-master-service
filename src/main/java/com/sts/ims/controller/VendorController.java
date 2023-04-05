package com.sts.ims.controller;

import com.sts.ims.dto.VendorDto;
import com.sts.ims.request.VendorListRequest;
import com.sts.ims.response.Response;
import com.sts.ims.service.VendorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor")
@CrossOrigin("*")
@Slf4j
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @GetMapping("{vendorId}")
    public Response getVendorById(@PathVariable Long vendorId) {
        log.info("Fetch vendor request for vendorId: {}", vendorId);
        return vendorService.getVendorById(vendorId);
    }

    @PostMapping("/list")
    public Response getVendorsList(@RequestBody VendorListRequest vendorListRequest) {
        log.info("Initiating vendor list fetch request");
        return vendorService.getVendorList(vendorListRequest.getPage(), vendorListRequest.getSize());
    }

    @PostMapping("/add")
    public Response addVendor(@RequestBody VendorDto vendorDto) {
        log.info("Request received for adding vendor");
        return vendorService.saveVendor(vendorDto);
    }

}
