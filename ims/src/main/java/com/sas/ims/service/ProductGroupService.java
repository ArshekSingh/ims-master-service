package com.sas.ims.service;

import com.sas.ims.dto.ProductGroupDto;
import com.sas.ims.response.Response;

public interface ProductGroupService {
	
	Response getActiveProductGroupDetails(ProductGroupDto dto);
	
	Response addProductGroup(ProductGroupDto dto);
	
	Response updateProductGroup(ProductGroupDto dto);
	
	Response getProductGroupDetailsById(Long productGroupId);
	
	Response softDeleteProductGroup(Long productGroupId);
	
	Response getParentGroup();

}
