package com.sts.ims.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BranchMasterDto {

    private Long branchId;
    private Long orgId;

    private String branchType;

    private Long parentId;

    private String branchCode;

    private String branchName;

    private LocalDateTime branchOpeningDate;

    private LocalDateTime branchClosingDate;

    private String status;

    private String address1;

    private String address2;

    private Integer stateId;

    private String city;

    private Integer pincode;

    private Long phoneNumber;

    private String emailId;

    private String partnerReffrenceCode;

    private String createdOn;
    private String modifiedOn;

}
