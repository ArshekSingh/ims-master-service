package com.sts.ims.controller;

import com.sts.ims.dto.BranchMasterDto;
import com.sts.ims.request.FilterRequest;
import com.sts.ims.response.Response;
import com.sts.ims.service.BranchService;
import com.sts.ims.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
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

}
