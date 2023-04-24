package com.sas.ims.utils;

import com.sas.ims.dto.ProductMasterDto;
import com.sas.ims.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

public class ValidationUtil {
    public static void validateAddProductRequest(ProductMasterDto dto) throws BadRequestException {
        if(dto.getProductGroupId() == null) {
            throw new BadRequestException("Product group id is required", HttpStatus.BAD_REQUEST);
        }
        if(!StringUtils.hasText(dto.getProductCode())) {
            throw new BadRequestException("Product code is required", HttpStatus.BAD_REQUEST);
        }
        if(!StringUtils.hasText(dto.getProductName())) {
            throw new BadRequestException("Product Name is required", HttpStatus.BAD_REQUEST);
        }
        if(!StringUtils.hasText(dto.getStatus())) {
            throw new BadRequestException("Status is required", HttpStatus.BAD_REQUEST);
        }
    }
}
