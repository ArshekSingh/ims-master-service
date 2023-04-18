package com.sas.ims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sas.ims.entity.DistrictMaster;
import com.sas.tokenlib.utils.UserSession;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistrictMasterDto {
    private Integer districtId;
    private String districtCode;
    private String districtName;
    private Integer stateId;
    private String active;
    private String stateCode;
    private String stateName;
    private Long orgId;
    private String insertedOn;
    private String insertedBy;
    private String updatedOn;
    private String updatedBy;

    public void mapDtoToEntityForDistrictUpdate(DistrictMasterDto districtMasterDto, DistrictMaster districtMaster, UserSession userSession) {
        districtMaster.setDistrictName(districtMasterDto.getDistrictName());
        districtMaster.setUpdatedOn(LocalDateTime.now());
        districtMaster.setUpdatedBy(userSession.getSub());
        districtMaster.setStateId(districtMasterDto.getStateId());
        districtMaster.setActive(districtMasterDto.getActive());
    }
}