package com.sas.ims.service;

import com.sas.ims.exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import com.sas.ims.dto.ProductMasterDto;
import com.sas.ims.response.Response;

public interface ProductMasterService {
	
	Response getActiveProductDetails(ProductMasterDto dto);
	
	Response addProductMaster(ProductMasterDto dto) throws BadRequestException;
	
	Response updateProductMaster(ProductMasterDto dto) throws BadRequestException;
	
	Response getProductDetailsById(Long productId);
	
	Response softDeleteProduct(Long productId);
	
	Response productUploaders(MultipartFile file);

}
