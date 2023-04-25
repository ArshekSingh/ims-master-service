package com.sas.ims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateMasterDto {
    private Integer stateId;
    private Integer countryId;
    private String stateCode;
    private String stateName;
    private String gstStateCode;
}