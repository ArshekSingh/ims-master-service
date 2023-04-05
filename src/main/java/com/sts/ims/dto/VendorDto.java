package com.sts.ims.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
public class VendorDto {
    private Long orgId;
    private Long vendorId;
    private String vendorName;
    private String vendorCode;
    private String vendorGroup;
    private String vendorType;
    private String address1;
    private String address2;
    private String address3;
    private Integer pincode;
    private String phone;
    private String telefax;
    private String emailId;
    private String website;
    private String mobileNumber;
    private String bankIfscCode;
    private String bankName;
    private String bankMmid;
    private String bankVpa;
    private String bankAccount;
    private String bankBranch;
    private String taxpayerNumber;
    private String pan;
    private String svctaxRegnum;
    private String source;
    private String gstin;
    private String stateId;
    private String gstrType;
    private String typeOfEnterprises;
    private String registrationNo;
    private String oneTimeVendor;
    private String vendorStatus;
    private String vendorApproved;
    private String documentPath;
    private String rcmFlag;
    private String vendorCreditLimit;
    private String approvalAuthority;
    private String vendorOpeningDate;
    private String vendorClosingDate;
}