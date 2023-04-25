package com.sas.ims.service;

import com.sas.ims.dto.ProductGroupDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.tokenlib.response.Response;

public interface ProductGroupService {
	
	Response getActiveProductGroupDetails(ProductGroupDto dto) throws BadRequestException;
	
	Response addProductGroup(ProductGroupDto dto);
	
	Response updateProductGroup(ProductGroupDto dto) throws BadRequestException;
	
	Response getProductGroupDetailsById(Long productGroupId) throws BadRequestException;
	
	Response softDeleteProductGroup(Long productGroupId);
	
	Response getParentGroup() throws BadRequestException;

}
