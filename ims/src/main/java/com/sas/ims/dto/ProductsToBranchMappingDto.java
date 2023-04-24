package com.sas.ims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductsToBranchMappingDto {
    private Integer branchId;
    List<Integer> assignedBranches;
    List<Integer> productList;
    List<ServerSideDropDownDto> assignedProducts;
    List<ServerSideDropDownDto> availableProducts;

    public boolean isValidRequest(ProductsToBranchMappingDto dto) {
        return dto != null && (dto.getBranchId() != null && dto.getAssignedProducts() != null);
    }

    public boolean isValidMappingRequest(ProductsToBranchMappingDto dto) {
        return dto != null && (dto.getAssignedBranches() != null && dto.getProductList() != null);
    }
}