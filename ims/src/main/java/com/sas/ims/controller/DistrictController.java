package com.sas.ims.controller;

import com.sas.ims.dto.DistrictMasterDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.service.DistrictService;
import com.sas.tokenlib.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/district")
public class DistrictController {

    private DistrictService districtService;

    @Autowired
    DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping("{districtId}")
    public Response getDistrictDetails(@PathVariable Integer districtId) throws ObjectNotFoundException {
        return districtService.getDistrictDetails(districtId);
    }

    @PostMapping("list")
    public Response getDistrictDetailList(@RequestBody FilterRequest filterRequest) throws BadRequestException {
        return districtService.getDistrictDetailList(filterRequest);
    }

    @PostMapping("addDistrict")
    public Response addDistrict(@RequestBody DistrictMasterDto districtMasterDto) {
        return districtService.addDistrict(districtMasterDto);
    }

    @PostMapping("updateDistrict")
    public Response updateDistrict(@RequestBody DistrictMasterDto districtMasterDto) throws ObjectNotFoundException {
        return districtService.updateDistrict(districtMasterDto);
    }
}