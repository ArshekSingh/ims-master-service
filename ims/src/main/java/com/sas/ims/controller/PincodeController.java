package com.sas.ims.controller;

import com.sas.ims.dto.PincodeMasterDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.service.PincodeService;
import com.sas.tokenlib.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/pincode")
public class PincodeController {

    private final PincodeService pincodeService;

    @Autowired
    PincodeController(PincodeService pincodeService) {
        this.pincodeService = pincodeService;
    }

    @GetMapping(value = "{pincode}")
    public Response getPincodeList(@PathVariable Integer pincode) {
        return pincodeService.getPincodeList(pincode);
    }

    @PostMapping("addPincode")
    public Response addPincode(@RequestBody PincodeMasterDto pincodeMasterDto) {
        return pincodeService.addPincode(pincodeMasterDto);
    }

    @PostMapping("updatePincode")
    public Response updatePincode(@RequestBody PincodeMasterDto pincodeMasterDto) throws ObjectNotFoundException {
        return pincodeService.updatePincode(pincodeMasterDto);
    }

    @GetMapping("getPincodeDetails/{pincode}")
    public Response getPincodeDetails(@PathVariable Integer pincode) throws ObjectNotFoundException {
        return pincodeService.getPincodeDetails(pincode);
    }

    @PostMapping("list")
    public Response getPincodeDetailsList(@RequestBody FilterRequest filterRequest) throws BadRequestException {
        return pincodeService.getPincodeDetailsList(filterRequest);
    }
}