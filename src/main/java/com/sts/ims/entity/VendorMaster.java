package com.sts.ims.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "VENDOR_MASTER")
public class VendorMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -4953090088706573889L;

    @Column(name = "ORG_ID")
    private Long ORG_ID;

    @Id
    @Column(name = "VENDOR_ID")
    private Long VENDOR_ID;

    @Column(name = "VENDOR_NAME")
    private String VENDOR_NAME;

    @Column(name = "VENDOR_GROUP")
    private String VENDOR_GROUP;

    @Column(name = "VENDOR_TYPE")
    private String VENDOR_TYPE;

    @Column(name = "ADDRESS1")
    private String ADDRESS1;

    @Column(name = "ADDRESS2")
    private String ADDRESS2;

    @Column(name = "ADDRESS3")
    private String ADDRESS3;

    @Column(name = "PINCODE")
    private Integer PINCODE;

    @Column(name = "PHONE")
    private String PHONE;

    @Column(name = "TELEFAX")
    private String TELEFAX;

    @Column(name = "EMAIL_ID")
    private String EMAIL_ID;

    @Column(name = "WEBSITE")
    private String WEBSITE;

    @Column(name = "MOBILE_NUMBER")
    private String MOBILE_NUMBER;

    @Column(name = "BANK_IFSC_CODE")
    private String BANK_IFSC_CODE;

    @Column(name = "BANK_NAME")
    private String BANK_NAME;

    @Column(name = "BANK_MMID")
    private String BANK_MMID;

    @Column(name = "BANK_VPA")
    private String BANK_VPA;

    @Column(name = "BANK_ACCOUNT")
    private String BANK_ACCOUNT;

    @Column(name = "BANK_BRANCH")
    private String BANK_BRANCH;

    @Column(name = "TAXPAYER_NUMBER")
    private String TAXPAYER_NUMBER;

    @Column(name = "PAN")
    private String PAN;

    @Column(name = "SVCTAX_REGNUM")
    private String SVCTAX_REGNUM;

    @Column(name = "SOURCE")
    private String SOURCE;

    @Column(name = "GSTIN")
    private String GSTIN;

    @Column(name = "STATE_ID")
    private String STATE_ID;

    @Column(name = "GSTR_TYPE")
    private String GSTR_TYPE;

    @Column(name = "TYPE_OF_ENTERPRISES")
    private String TYPE_OF_ENTERPRISES;

    @Column(name = "REGISTRATION_NO")
    private String REGISTRATION_NO;

    @Column(name = "ONE_TIME_VENDOR")
    private String ONE_TIME_VENDOR;

    @Column(name = "VENDOR_STATUS")
    private String VENDOR_STATUS;

    @Column(name = "DOCUMENT_PATH")
    private String DOCUMENT_PATH;

    @Column(name = "RCM_FLAG")
    private String RCM_FLAG;

    @Column(name = "VENDOR_OPENING_DATE")
    private LocalDateTime VENDOR_OPENING_DATE;

    @Column(name = "VENDOR_CLOSING_DATE")
    private LocalDateTime VENDOR_CLOSING_DATE;

}
