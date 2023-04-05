package com.sts.ims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sts.ims.dto.ProductMasterDto;
import com.sts.ims.exception.BadRequestException;
import com.sts.ims.exception.ObjectNotFoundException;
import com.sts.ims.response.Response;
import com.sts.ims.service.ProductMasterService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
@Slf4j
public class ProductMasterController {
	
	@Autowired
	ProductMasterService productMasterService;
	

    @PostMapping("/addProduct")
    public Response addProductMaster(@RequestBody ProductMasterDto dto) throws ObjectNotFoundException, BadRequestException {
    	log.info("addProductMaster ");
        return productMasterService.addProductMaster(dto);
    }

    @PostMapping("/products")
    public Response getProductList(@RequestBody ProductMasterDto dto) throws BadRequestException {
        return productMasterService.getActiveProductDetails(dto);
    }

    @GetMapping(value = "/product/{productId}")
    public Response getProductDetailsById(@PathVariable Long productId) throws ObjectNotFoundException {
        return productMasterService.getProductDetailsById(productId);
    }

    @PostMapping("/updateProduct")
    public Response updateProduct(@RequestBody ProductMasterDto dto) throws ObjectNotFoundException, BadRequestException {
        return productMasterService.updateProductMaster(dto);
    }
    
    @PutMapping("/delete")
    public Response softDeleteProduct(@PathVariable Long productId) throws ObjectNotFoundException, BadRequestException {
        return productMasterService.softDeleteProduct(productId);
    }
    
    @PostMapping("/productUploaders")
    public Response productUploaders(@RequestPart("file") MultipartFile file) throws BadRequestException {
        return productMasterService.productUploaders(file);
    }

}
