package com.sas.ims.controller;

import com.sas.ims.dto.IfscMasterDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.service.IfscService;
import com.sas.tokenlib.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "api/ifsc")
public class IfscController {

    private final IfscService ifscService;

    @GetMapping(value = "{ifscCode}")
    public Response getIfscDetails(@PathVariable String ifscCode) throws ObjectNotFoundException {
        return ifscService.getIfscDetails(ifscCode);
    }

    @GetMapping(value = "/ifscCode/{ifscCode}")
    public Response getIfscCodeList(@PathVariable String ifscCode) {
        return ifscService.getIfscCodeList(ifscCode);
    }

    @PostMapping("addIfscCode")
    public Response addIfscCode(@RequestBody IfscMasterDto ifscMasterDto) {
        return ifscService.addIfscCode(ifscMasterDto);
    }

    @PostMapping("updateIfscCode")
    public Response updateIfscCode(@RequestBody IfscMasterDto ifscMasterDto) throws ObjectNotFoundException {
        return ifscService.updateIfscCode(ifscMasterDto);
    }

    @GetMapping("getIfscCodeDetails/{ifscCode}")
    public Response getIfscCodeDetails(@PathVariable String ifscCode) throws ObjectNotFoundException {
        return ifscService.getIfscCodeDetails(ifscCode);
    }

    @PostMapping("list")
    public Response getIfscCodeDetailsList(@RequestBody FilterRequest filterRequest) throws BadRequestException {
        return ifscService.getIfscCodeDetailsList(filterRequest);
    }
}