package com.sas.ims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductsToAreaMappingDto {
    private Long areaId;
    List<Long> assignedAreas;
    List<Long> productList;
    List<ServerSideDropDownDto> assignedProducts;
    List<ServerSideDropDownDto> availableProducts;

    public boolean isValidRequest(ProductsToAreaMappingDto dto) {
        return dto != null && (dto.getAreaId() != null && dto.getAssignedProducts() != null);
    }

    public boolean isValidMappingRequest(ProductsToAreaMappingDto dto) {
        return dto != null && (dto.getAssignedAreas() != null && dto.getProductList() != null);
    }
}