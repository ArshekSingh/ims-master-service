package com.sas.ims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AreasToVendorMappingDto {
    private Long vendorId;
    List<Long> assignedVendors;
    List<ServerSideDropDownDto> assignedAreas;
    List<ServerSideDropDownDto> availableAreas;

    public boolean isValidRequest(AreasToVendorMappingDto dto) {
        return dto != null && (dto.getVendorId() != null && dto.getAssignedAreas() != null);
    }

    public boolean isValidMappingRequest(AreasToVendorMappingDto dto) {
        return dto != null && (dto.getAssignedVendors() != null && dto.getAssignedAreas() != null);
    }
}
