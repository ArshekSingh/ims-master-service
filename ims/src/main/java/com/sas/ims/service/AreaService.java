package com.sas.ims.service;

import com.sas.ims.dto.ProductsToAreaMappingDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.InternalServerErrorException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.dto.AreaMasterDto;
import com.sas.ims.request.FilterRequest;
import com.sas.tokenlib.response.Response;

public interface AreaService {
    Response getAreaDetail(Long branchId) throws ObjectNotFoundException;

    Response getAreaDetailList(FilterRequest filterRequest) throws BadRequestException;

    Response addArea(AreaMasterDto areaMasterDto) throws ObjectNotFoundException, BadRequestException;

    Response updateArea(AreaMasterDto areaMasterDto) throws ObjectNotFoundException, BadRequestException;

    Response getAreaTypeMap() throws ObjectNotFoundException, InternalServerErrorException;

    Response parentAreaCodeMap(String areaType) throws InternalServerErrorException;

    Response areaCodeMap(String areaType) throws InternalServerErrorException;

    Response getProductsAssignedOrAvailableToArea(Long areaId);

    Response assignProductsToBranch(ProductsToAreaMappingDto productsToBranchMappingDto);
}
