package com.sas.ims.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "DISTRICT_MASTER")
public class DistrictMaster extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2338626292552177485L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISTRICT_ID")
    private Integer districtId;

    @Column(name = "DISTRICT_NAME")
    private String districtName;

    @Column(name = "ACTIVE")
    private String active;

    @Column(name = "COUNTRY_ID")
    private Integer countryId;

    @Column(name = "DISTRICT_CODE")
    private String districtCode;

    @Column(name = "STATE_ID")
    private Integer stateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATE_ID", referencedColumnName = "STATE_ID", insertable = false, updatable = false)
    private StateMaster stateMaster;
}