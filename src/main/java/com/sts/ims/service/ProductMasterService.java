package com.sas.service;

import com.sas.dto.ProductMasterDto;
import com.sas.response.Response;

public interface ProductMasterService {
	
	Response getActiveProductDetails(ProductMasterDto dto);
	
	Response addProductMaster(ProductMasterDto dto);
	
	Response updateProductMaster(ProductMasterDto dto);
	
	Response getProductDetailsById(Long productId);

}
