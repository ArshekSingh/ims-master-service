package com.sas.ims.controller;

import com.sas.ims.exception.BadRequestException;
import com.sas.ims.dto.BranchMasterDto;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.response.Response;
import com.sas.ims.service.BranchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/branch")
@CrossOrigin(origins = "*")
@Slf4j
public class BranchController {

    @Autowired
    private BranchService branchService;


    @GetMapping(value = "/{branchId}")
    public Response getBranchDetail(@PathVariable Long branchId) throws ObjectNotFoundException {
        return branchService.getBranchDetail(branchId);
    }

    @PostMapping("/list")
    public Response getBranchDetailList(@RequestBody FilterRequest filterRequest) throws BadRequestException, ObjectNotFoundException {
        return branchService.getBranchDetailList(filterRequest);
    }

    @PostMapping("/addBranch")
    public Response addBranch(@RequestBody BranchMasterDto branchMasterDto) throws ObjectNotFoundException, BadRequestException {
        return branchService.addBranch(branchMasterDto);
    }

    @PostMapping("/updateBranch")
    public Response updateBranch(@RequestBody BranchMasterDto branchMasterDto) throws ObjectNotFoundException {
        return branchService.updateBranch(branchMasterDto);
    }

    @GetMapping(value = "getProductListAssignedToBranch/{branchId}")
    public Response getProductListAssignedToBranch(@PathVariable Long branchId) {
        log.info("getProductListAssignedToBranch() invoked for branchId : {}", branchId);
        return branchService.getProductsAssignedOrAvailableToBranch(branchId);
    }

}
