package com.sas.ims.controller;

import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
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
@RequestMapping("/api")
@CrossOrigin("*")
public class ProductGroupController {
	
	@Autowired
	ProductGroupService productGroupService;
	

    @PostMapping("/addProductGroup")
    public Response addProductMaster(@RequestBody ProductGroupDto dto) throws ObjectNotFoundException, BadRequestException {
        return productGroupService.addProductGroup(dto);
    }

    @PostMapping("/productGroups")
    public Response getProductList(@RequestBody ProductGroupDto dto) throws BadRequestException {
        return productGroupService.getActiveProductGroupDetails(dto);
    }

    @GetMapping(value = "/productGroup/{productId}")
    public Response getProductDetailsById(@PathVariable Long productId) throws ObjectNotFoundException {
        return productGroupService.getProductGroupDetailsById(productId);
    }

    @PostMapping("/updateProductGroup")
    public Response updateProduct(@RequestBody ProductGroupDto dto) throws ObjectNotFoundException, BadRequestException {
        return productGroupService.updateProductGroup(dto);
    }
    
//    @PutMapping("/delete")
//    public Response softDeleteProduct(@PathVariable Long productId) throws ObjectNotFoundException, BadRequestException {
//        return productGroupService.softDeleteProductGroup(productId);
//    }
    
    @GetMapping(value = "/parentGroup")
    public Response getParentGroup(){
        return productGroupService.getParentGroup();
    }
    
}
