package com.sas.ims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sas.ims.entity.IfscMaster;
import com.sas.tokenlib.utils.UserSession;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IfscMasterDto extends BaseDto {
    private String ifscCode;
    private Integer countryId;
    private String bankName;
    private String bankBranch;
    private String branchAddress;
    private String city;
    private Integer state;
    private String phoneNo;
    private String status;
    private String stateName;

    public void mapDtoToEntityForIfscUpdate(IfscMasterDto ifscMasterDto, IfscMaster ifscMaster, UserSession userSession) {
        ifscMaster.setBankBranch(ifscMasterDto.getBankBranch());
        ifscMaster.setBankName(ifscMasterDto.getBankName());
        ifscMaster.setBranchAddress(ifscMasterDto.getBranchAddress());
        ifscMaster.setCity(ifscMasterDto.getCity());
        ifscMaster.setState(ifscMasterDto.getState());
        ifscMaster.setPhoneNo(ifscMasterDto.getPhoneNo());
        ifscMaster.setUpdatedOn(LocalDateTime.now());
        ifscMaster.setUpdatedBy(userSession.getSub());
    }
}