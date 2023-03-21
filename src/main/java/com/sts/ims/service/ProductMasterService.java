package com.sts.ims.service;

import com.sts.ims.dto.ProductMasterDto;
import com.sts.ims.response.Response;

public interface ProductMasterService {
	
	Response getActiveProductDetails(ProductMasterDto dto);
	
	Response addProductMaster(ProductMasterDto dto);
	
	Response updateProductMaster(ProductMasterDto dto);
	
	Response getProductDetailsById(Long productId);

}
