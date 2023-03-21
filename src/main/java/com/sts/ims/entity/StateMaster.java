package com.sts.ims.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "STATE_MASTER")
public class StateMaster extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2338626292552177485L;

    @Id
    @Column(name = "STATE_ID")
    private Integer stateId;

    @Column(name = "COUNTRY_ID")
    private Integer countryId;

    @Column(name = "STATE_CODE")
    private String stateCode;

    @Column(name = "STATE_NAME")
    private String stateName;

    @Column(name = "GST_STATE_CODE")
    private String gstStateCode;
}