package com.sas.ims.service;

import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.dto.BranchMasterDto;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.response.Response;

public interface BranchService {
    Response getBranchDetail(Long branchId) throws ObjectNotFoundException;

    Response getBranchDetailList(FilterRequest filterRequest) throws BadRequestException;

    Response addBranch(BranchMasterDto branchMasterDto) throws ObjectNotFoundException, BadRequestException;

    Response updateBranch(BranchMasterDto branchMasterDto) throws ObjectNotFoundException;

    Response getProductsAssignedOrAvailableToBranch(Long branchId);
}
