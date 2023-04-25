package com.sas.ims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sas.ims.entity.PincodeMaster;
import com.sas.tokenlib.utils.UserSession;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PincodeMasterDto {
    private Integer countryId;
    private Integer pincode;
    private StateMasterDto state;
    private DistrictMasterDto district;
    private String active;
    private Integer districtId;
    private Integer stateId;
    private String stateName;
    private String districtName;

    public void mapDtoToEntityForPincodeUpdate(PincodeMasterDto pincodeMasterDto, PincodeMaster pincodeMaster, UserSession userSession) {
        pincodeMaster.setDistrictId(pincodeMasterDto.getDistrictId());
        pincodeMaster.setStateId(pincodeMasterDto.getStateId());
        pincodeMaster.setUpdatedOn(LocalDateTime.now());
        pincodeMaster.setUpdatedBy(userSession.getSub());
    }
}