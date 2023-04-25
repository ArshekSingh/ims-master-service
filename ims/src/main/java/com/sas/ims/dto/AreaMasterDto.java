package com.sas.ims.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AreaMasterDto {

    private Long areaId;

    private Long orgId;

    private String areaType;

    private Long parentId;

    private String areaCode;

    private String areaName;

    private String areaOpeningDate;

    private String areaClosingDate;

    private String status;

    private String address1;

    private String address2;

    private Integer stateId;

    private String city;

    private Integer pincode;

    private Long phoneNumber;

    private String emailId;

    private String partnerReferenceCode;

    private String createdOn;

    private String modifiedOn;

}
