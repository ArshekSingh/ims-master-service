package com.sas.ims.controller;

import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sas.ims.dto.ProductGroupDto;
import com.sas.ims.response.Response;
import com.sas.ims.service.ProductGroupService;

@RestController
@RequestMapping("/api/productGroup")
@CrossOrigin("*")
@Slf4j
public class ProductGroupController {
	
	@Autowired
	ProductGroupService productGroupService;
	

    @PostMapping("/addProductGroup")
    public Response addProductMaster(@RequestBody ProductGroupDto dto) throws ObjectNotFoundException, BadRequestException {
        return productGroupService.addProductGroup(dto);
    }

    @PostMapping("/getProductList")
    public Response getProductList(@RequestBody ProductGroupDto dto) throws BadRequestException {
        return productGroupService.getActiveProductGroupDetails(dto);
    }

    @GetMapping(value = "/getProductDetailsByProductGroupId/{productGroupId}")
    public Response getProductDetailsById(@PathVariable Long productGroupId) throws ObjectNotFoundException, BadRequestException {
        log.info("Request initiated to revoke getProductGroupDetailsById method for productGroupId {}", productGroupId);
        return productGroupService.getProductGroupDetailsById(productGroupId);
    }

    @PostMapping("/updateProductGroup")
    public Response updateProduct(@RequestBody ProductGroupDto dto) throws ObjectNotFoundException, BadRequestException {
        log.info("Request initiated to call method updateProductGroup for productGroupId {}", dto.getProductGroupId());
        return productGroupService.updateProductGroup(dto);
    }
    
//    @PutMapping("/delete")
//    public Response softDeleteProduct(@PathVariable Long productId) throws ObjectNotFoundException, BadRequestException {
//        return productGroupService.softDeleteProductGroup(productId);
//    }
    
    @GetMapping(value = "/parentGroup")
    public Response getParentGroup() throws BadRequestException {
        return productGroupService.getParentGroup();
    }
    
}
