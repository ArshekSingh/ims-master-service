package com.sas.ims.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORG_MASTER")
public class OrganisationMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8429414798376337020L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ORG_ID")
    private Long orgId;

    @Column(name = "ORG_CODE")
    private String orgCode;

    @Column(name = "ORG_NAME")
    private String orgName;

    @Column(name = "HO_ADDRESS_1")
    private String hoAddress1;

    @Column(name = "HO_ADDRESS_2")
    private String hoAddress2;

    @Column(name = "HO_ADDRESS_3")
    private String hoAddress3;

    @Column(name = "HO_PINCODE")
    private Integer hoPinCode;

    @Column(name = "HO_STATE_ID")
    private String hoStateId;

    @Column(name = "HO_COUNTRY")
    private String hoCountry;

    @Column(name = "REG_ADDRESS_1")
    private String regAddress1;

    @Column(name = "REG_ADDRESS_2")
    private String regAddress2;

    @Column(name = "REG_ADDRESS_3")
    private String regAddress3;

    @Column(name = "REG_PINCODE")
    private Integer regPincode;

    @Column(name = "REG_STATE_ID")
    private String regStateId;

    @Column(name = "LANDLINE_1")
    private String landline1;

    @Column(name = "LANDLINE_2")
    private String landline2;

    @Column(name = "MOBILE_1")
    private String mobile1;

    @Column(name = "MOBILE_2")
    private String mobile2;

    @Column(name = "FAX")
    private String fax;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "WEBSITE")
    private String website;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CURRENCY_CODE")
    private String currencyCode;

    @Column(name = "PAN_NUMBER")
    private String panNUmber;

    @Column(name = "VAT_NUMBER")
    private String vatNumber;

    @Column(name = "GST_NUMBER")
    private String gstNumber;

    @Column(name = "TAX_REG_NUMBER")
    private String taxRegNumber;

    @Column(name = "TERMS_CODE")
    private String termsCode;

    @Column(name = "LOGO_PATH")
    private String logoPath;

    @Column(name = "APP_LOGOUT_TIME")
    private LocalDateTime appLogoutTime;

    @Column(name = "APP_LOGIN_TIME")
    private LocalDateTime appLoginTime;
}
