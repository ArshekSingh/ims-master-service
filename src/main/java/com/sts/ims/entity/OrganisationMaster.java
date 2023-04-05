package com.sts.ims.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORG_MASTER")
public class OrganisationMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8429414798376337020L;

    @Id
    @Column(name = "ORG_ID")
    private Long ORG_ID;

    @Column(name = "ORG_CODE")
    private String ORG_CODE;

    @Column(name = "ORG_NAME")
    private String ORG_NAME;

    @Column(name = "HO_ADDRESS_1")
    private String HO_ADDRESS_1;

    @Column(name = "HO_ADDRESS_2")
    private String HO_ADDRESS_2;

    @Column(name = "HO_ADDRESS_3")
    private String HO_ADDRESS_3;

    @Column(name = "HO_PINCODE")
    private Integer HO_PINCODE;

    @Column(name = "HO_STATE_ID")
    private String HO_STATE_ID;

    @Column(name = "HO_COUNTRY")
    private String HO_COUNTRY;

    @Column(name = "REG_ADDRESS_1")
    private String REG_ADDRESS_1;

    @Column(name = "REG_ADDRESS_2")
    private String REG_ADDRESS_2;

    @Column(name = "REG_ADDRESS_3")
    private String REG_ADDRESS_3;

    @Column(name = "REG_PINCODE")
    private Integer REG_PINCODE;

    @Column(name = "REG_STATE_ID")
    private String REG_STATE_ID;

    @Column(name = "LANDLINE_1")
    private String LANDLINE_1;

    @Column(name = "LANDLINE_2")
    private String LANDLINE_2;

    @Column(name = "MOBILE_1")
    private String MOBILE_1;

    @Column(name = "MOBILE_2")
    private String MOBILE_2;

    @Column(name = "FAX")
    private String FAX;

    @Column(name = "EMAIL")
    private String EMAIL;

    @Column(name = "WEBSITE")
    private String WEBSITE;

    @Column(name = "STATUS")
    private String STATUS;

    @Column(name = "CURRENCY_CODE")
    private String CURRENCY_CODE;

    @Column(name = "PAN_NUMBER")
    private String PAN_NUMBER;

    @Column(name = "VAT_NUMBER")
    private String VAT_NUMBER;

    @Column(name = "GST_NUMBER")
    private String GST_NUMBER;

    @Column(name = "TAX_REG_NUMBER")
    private String TAX_REG_NUMBER;

    @Column(name = "TERMS_CODE")
    private String TERMS_CODE;

    @Column(name = "LOGO_PATH")
    private String LOGO_PATH;

    @Column(name = "APP_LOGOUT_TIME")
    private LocalDateTime APP_LOGOUT_TIME;

    @Column(name = "APP_LOGIN_TIME")
    private LocalDateTime APP_LOGIN_TIME;
}
