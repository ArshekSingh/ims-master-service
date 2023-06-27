package com.sas.ims.dto;

import com.sas.ims.entity.VendorMaster;
import com.sas.ims.utils.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;

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

    private VendorDto vendorMasterToDto(VendorMaster vendorMaster) {
        VendorDto vendorDto = new VendorDto();
        vendorDto.setOrgId(vendorMaster.getOrgId());
        vendorDto.setVendorCode(vendorMaster.getVendorCode());
        vendorDto.setVendorId(vendorMaster.getVendorId());
        vendorDto.setVendorName(vendorMaster.getVendorName());
        vendorDto.setVendorGroup(vendorMaster.getVendorGroup());
        vendorDto.setVendorType(vendorMaster.getVendorType());
        vendorDto.setAddress1(vendorMaster.getAddress1());
        vendorDto.setAddress2(vendorMaster.getAddress2());
        vendorDto.setAddress3(vendorMaster.getAddress3());
        vendorDto.setPincode(vendorMaster.getPincode());
        vendorDto.setPhone(vendorMaster.getPhone());
        vendorDto.setTelefax(vendorMaster.getTelefax());
        vendorDto.setEmailId(vendorMaster.getEmailId());
        vendorDto.setWebsite(vendorMaster.getWebsite());
        vendorDto.setMobileNumber(vendorMaster.getMobileNumber());
        vendorDto.setBankIfscCode(vendorMaster.getBankIfscCode());
        vendorDto.setBankName(vendorMaster.getBankName());
        vendorDto.setBankMmid(vendorMaster.getBankMmid());
        vendorDto.setBankVpa(vendorMaster.getBankVpa());
        vendorDto.setBankAccount(vendorMaster.getBankAccount());
        vendorDto.setBankBranch(vendorMaster.getBankBranch());
        vendorDto.setTaxpayerNumber(vendorMaster.getTaxpayerNumber());
        vendorDto.setPan(vendorMaster.getPan());
        vendorDto.setSvctaxRegnum(vendorMaster.getSvctaxRegnum());
        vendorDto.setSource(vendorMaster.getSource());
        vendorDto.setGstin(vendorMaster.getGstin());
        vendorDto.setStateId(vendorMaster.getStateId());
        vendorDto.setGstrType(vendorMaster.getGstrType());
        vendorDto.setTypeOfEnterprises(vendorMaster.getTypeOfEnterprises());
        vendorDto.setRegistrationNo(vendorMaster.getRegistrationNo());
        vendorDto.setOneTimeVendor(vendorMaster.getOneTimeVendor());
        vendorDto.setVendorStatus(vendorMaster.getVendorStatus());
        vendorDto.setDocumentPath(vendorMaster.getDocumentPath());
        vendorDto.setRcmFlag(vendorMaster.getRcmFlag());
        vendorDto.setVendorOpeningDate(DateTimeUtil.dateTimeToString(vendorMaster.getVendorOpeningDate(), DateTimeUtil.DDMMYYYY));
        vendorDto.setVendorClosingDate(DateTimeUtil.dateTimeToString(vendorMaster.getVendorClosingDate(), DateTimeUtil.DDMMYYYY));
        return vendorDto;
    }
}