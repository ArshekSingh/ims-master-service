package com.sas.ims.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "PINCODE_MASTER")
public class PincodeMaster extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2338626292552177485L;

    @EmbeddedId
    private PincodeMasterPK pincodeMasterPK;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATE_ID", referencedColumnName = "STATE_ID", insertable = false, updatable = false)
    private StateMaster stateMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISTRICT_ID", referencedColumnName = "DISTRICT_ID", insertable = false, updatable = false)
    private DistrictMaster districtMaster;

    @Column(name = "DISTRICT_ID")
    private Integer districtId;

    @Column(name = "STATE_ID")
    private Integer stateId;

    @Column(name = "ACTIVE")
    private String active;
}