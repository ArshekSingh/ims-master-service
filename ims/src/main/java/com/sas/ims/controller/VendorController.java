package com.sas.ims.controller;

import com.sas.ims.dto.ProductsToAreaMappingDto;
import com.sas.ims.dto.ProductsToVendorMappingDto;
import com.sas.ims.dto.VendorDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.request.VendorListRequest;
import com.sas.ims.service.VendorService;
import com.sas.tokenlib.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/remove/{vendorId}")
    public Response removeVendor(@PathVariable Long vendorId) {
        log.info("Request received for adding vendor");
        return vendorService.removeVendor(vendorId);
    }

    @GetMapping(value = "/getProductListAssignedToVendor/{vendorId}")
    public Response getProductListAssignedToVendor(@PathVariable Long vendorId) {
        log.info("getProductListAssignedToVendor() invoked for vendorId : {}", vendorId);
        return vendorService.getProductsAssignedOrAvailableToVendor(vendorId);
    }

    @PostMapping(value = "/assignVendorToProductList")
    public Response assignVendorToProductList(@RequestBody ProductsToVendorMappingDto productsToVendorMappingDto) throws BadRequestException {
        if (productsToVendorMappingDto.isValidRequest(productsToVendorMappingDto)) {
            log.info("assignVendorToProductList() invoked for areaId : {}", productsToVendorMappingDto.getVendorId());
            return vendorService.assignProductsToVendor(productsToVendorMappingDto);
        }
        log.error("productsToVendorMappingDto failed validation check at assignVendorToProductList() ,vendorId : {}", productsToVendorMappingDto.getVendorId());
        throw new BadRequestException("Pass all required attributes", HttpStatus.BAD_REQUEST);
    }

}
