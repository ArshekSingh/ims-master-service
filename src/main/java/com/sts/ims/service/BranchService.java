package com.sts.ims.service;

import com.sts.ims.dto.BranchMasterDto;
import com.sts.ims.exception.BadRequestException;
import com.sts.ims.exception.ObjectNotFoundException;
import com.sts.ims.request.FilterRequest;
import com.sts.ims.response.Response;

public interface BranchService {
    Response getBranchDetail(Long branchId) throws ObjectNotFoundException;

    Response getBranchDetailList(FilterRequest filterRequest) throws BadRequestException;

    Response addBranch(BranchMasterDto branchMasterDto) throws ObjectNotFoundException, BadRequestException;

    Response updateBranch(BranchMasterDto branchMasterDto) throws ObjectNotFoundException;
}
