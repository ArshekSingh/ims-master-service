package com.sas.ims.entity;

import lombok.Data;

import javax.persistence.*;


import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "AREA_MASTER")
@Data
public class AreaMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -6310813846153493716L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AREA_ID")
    private Long areaId;

    @Column(name = "ORG_ID")
    private Long orgId;

    @Column(name = "AREA_TYPE")
    private String areaType;

    @Column(name = "PARENT_ID")
    private Integer parentId;

    @Column(name = "AREA_CODE")
    private String areaCode;

    @Column(name = "AREA_NAME")
    private String areaName;

    @Column(name = "AREA_OPENING_DATE")
    private LocalDateTime areaOpeningDate;

    @Column(name = "AREA_CLOSING_DATE")
    private LocalDateTime areaClosingDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ADDRESS_1")
    private String address1;

    @Column(name = "ADDRESS_2")
    private String address2;

    @Column(name = "STATE_ID")
    private Integer stateId;

    @Column(name = "CITY")
    private String city;

    @Column(name = "PINCODE")
    private Integer pincode;

    @Column(name = "PHONE_NUMBER")
    private Long phoneNumber;

    @Column(name = "EMAIL_ID")
    private String emailId;

    @Column(name = "PARTNER_REFERENCE_CODE")
    private String partnerReferenceCode;

    @Column(name = "AREA_MANAGER_ID")
    private String areaManagerId;

}
