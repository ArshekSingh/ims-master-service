package com.sas.ims.controller;

import com.sas.ims.dto.AreasToVendorMappingDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.InternalServerErrorException;
import com.sas.ims.service.VendorAreaMappingService;
import com.sas.tokenlib.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mapping")
@CrossOrigin("*")
@Slf4j
public class VendorAreaMappingController {

    @Autowired
    private VendorAreaMappingService vendorAreaMappingService;

    @GetMapping(value = "getAreaListAssignedToVendor/{vendorId}")
    public Response getAreaListAssignedToVendor(@PathVariable Long vendorId) {
        log.info("getAreaListAssignedToVendor() invoked for areaId : {}", vendorId);
        return vendorAreaMappingService.getAreasAssignedOrAvailableToVendor(vendorId);
    }

    @PostMapping(value = "assignVendorToAreaList")
    public Response assignVendorToAreaList(@RequestBody AreasToVendorMappingDto areasToVendorMappingDto) throws BadRequestException {
        if (areasToVendorMappingDto.isValidRequest(areasToVendorMappingDto)) {
            log.info("assignVendorToAreaList() invoked for vendorId : {}", areasToVendorMappingDto.getVendorId());
            return vendorAreaMappingService.assignAreasToVendor(areasToVendorMappingDto);
        }
        log.error("areasToVendorMappingDto failed validation check at assignVendorToAreaList() ,vendorId : {}", areasToVendorMappingDto.getVendorId());
        throw new BadRequestException("Pass all required attributes", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "vendorList")
    public Response getVendorsList() throws BadRequestException, InternalServerErrorException {
        return vendorAreaMappingService.getVendorsList();
    }
}
