package com.sas.ims.controller;

import com.sas.ims.dto.VendorDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.request.VendorListRequest;
import com.sas.ims.response.Response;
import com.sas.ims.service.VendorService;
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

    @PostMapping("/update")
    public Response updateVendor(@RequestBody VendorDto vendorDto) throws BadRequestException {
        log.info("Initiating vendor update request");
        return vendorService.updateVendor(vendorDto);
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
