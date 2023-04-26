package com.sas.ims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductsToVendorMappingDto {
    private Long vendorId;
    List<Long> assignedVendors;
    List<Long> productList;
    List<ServerSideDropDownDto> assignedProducts;
    List<ServerSideDropDownDto> availableProducts;

    public boolean isValidRequest(ProductsToVendorMappingDto dto) {
        return dto != null && (dto.getVendorId() != null && dto.getAssignedProducts() != null);
    }

    public boolean isValidMappingRequest(ProductsToVendorMappingDto dto) {
        return dto != null && (dto.getAssignedVendors() != null && dto.getProductList() != null);
    }
}
