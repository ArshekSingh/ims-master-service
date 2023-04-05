package com.sts.ims.service;

import com.sts.ims.dto.ProductGroupDto;
import com.sts.ims.response.Response;

public interface ProductGroupService {
	
	Response getActiveProductGroupDetails(ProductGroupDto dto);
	
	Response addProductGroup(ProductGroupDto dto);
	
	Response updateProductGroup(ProductGroupDto dto);
	
	Response getProductGroupDetailsById(Long productGroupId);
	
	Response softDeleteProductGroup(Long productGroupId);
	
	Response getParentGroup();

}
