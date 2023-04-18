package com.sas.ims.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "IFSC_MASTER")
public class IfscMaster extends BaseEntity implements Serializable {

    private final static long serialVersionUID = -6524768694427900621L;

    @Id
    @Column(name = "IFSC_CODE")
    private String ifscCode;

    @Column(name = "COUNTRY_ID")
    private Integer countryId;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "BANK_BRANCH")
    private String bankBranch;

    @Column(name = "BRANCH_ADDRESS")
    private String branchAddress;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    private Integer state;

    @Column(name = "PHONE_NO")
    private String phoneNo;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATE", referencedColumnName = "STATE_ID", insertable = false, updatable = false)
    private StateMaster stateMaster;
}