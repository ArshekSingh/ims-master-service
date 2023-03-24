package com.sts.ims.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "VENDOR_MASTER")
@Getter
@Setter
public class VendorMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -4953090088706573889L;

    @Column(name = "ORG_ID")
    private Long orgId;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "VENDOR_ID")
    private Long vendorId;

    @Column(name = "VENDOR_NAME")
    private String vendorName;

    @Column(name = "VENDOR_CODE")
    private String vendorCode;

    @Column(name = "VENDOR_GROUP")
    private String vendorGroup;

    @Column(name = "VENDOR_TYPE")
    private String vendorType;

    @Column(name = "ADDRESS1")
    private String address1;

    @Column(name = "ADDRESS2")
    private String address2;

    @Column(name = "ADDRESS3")
    private String address3;

    @Column(name = "PINCODE")
    private Integer pincode;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "TELEFAX")
    private String telefax;

    @Column(name = "EMAIL_ID")
    private String emailId;

    @Column(name = "WEBSITE")
    private String website;

    @Column(name = "MOBILE_NUMBER")
    private String mobileNumber;

    @Column(name = "BANK_IFSC_CODE")
    private String bankIfscCode;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "BANK_MMID")
    private String bankMmid;

    @Column(name = "BANK_VPA")
    private String bankVpa;

    @Column(name = "BANK_ACCOUNT")
    private String bankAccount;

    @Column(name = "BANK_BRANCH")
    private String bankBranch;

    @Column(name = "TAXPAYER_NUMBER")
    private String taxpayerNumber;

    @Column(name = "PAN")
    private String pan;

    @Column(name = "SVCTAX_REGNUM")
    private String svctaxRegnum;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "GSTIN")
    private String gstin;

    @Column(name = "STATE_ID", nullable = false)
    private String stateId;

    @Column(name = "GSTR_TYPE")
    private String gstrType;

    @Column(name = "TYPE_OF_ENTERPRISES")
    private String typeOfEnterprises;

    @Column(name = "REGISTRATION_NO")
    private String registrationNo;

    @Column(name = "ONE_TIME_VENDOR")
    private String oneTimeVendor;

    @Column(name = "VENDOR_STATUS")
    private String vendorStatus;

    @Column(name = "VENDOR_APPROVED")
    private String vendorApproved;

    @Column(name = "DOCUMENT_PATH")
    private String documentPath;

    @Column(name = "RCM_FLAG")
    private String rcmFlag;

    @Column(name = "VENDOR_CREDIT_LIMIT")
    private String vendorCreditLimit;

    @Column(name = "APPROVAL_AUTHORITY")
    private String approvalAuthority;

    @Column(name = "VENDOR_OPENING_DATE")
    private LocalDateTime vendorOpeningDate;

    @Column(name = "VENDOR_CLOSING_DATE")
    private LocalDateTime vendorClosingDate;
}
