package com.sas.ims.service;

import com.sas.ims.exception.BadRequestException;
import com.sas.tokenlib.response.Response;
import org.springframework.web.multipart.MultipartFile;

import com.sas.ims.dto.ProductMasterDto;

public interface ProductMasterService {
	
	Response getActiveProductDetails(ProductMasterDto dto);
	
	Response addProductMaster(ProductMasterDto dto) throws BadRequestException;
	
	Response updateProductMaster(ProductMasterDto dto) throws BadRequestException;
	
	Response getProductDetailsById(Long productId);
	
	Response softDeleteProduct(Long productId);
	
	Response productUploaders(MultipartFile file);

}
