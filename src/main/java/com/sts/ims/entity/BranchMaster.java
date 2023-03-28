package com.sts.ims.entity;

import lombok.Data;

import javax.persistence.*;


import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "BRANCH_MASTER")
@Data
public class BranchMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 9182562580678493055L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BRANCH_ID")
    private Long branchId;
    @Column(name = "ORG_ID")
    private Long orgId;

    @Column(name = "BRANCH_TYPE")
    private String branchType;

    @Column(name = "PARENT_ID")
    private Integer parent_id;

    @Column(name = "BRANCH_CODE")
    private String branchCode;

    @Column(name = "BRANCH_NAME")
    private String branchName;

    @Column(name = "BRANCH_OPENING_DATE")
    private LocalDateTime branchOpeningDate;

    @Column(name = "BRANCH_CLOSING_DATE")
    private LocalDateTime branchClosingDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ADDRESS", nullable = false)
    private String ADDRESS;
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

    @Column(name = "PARTNER_REFFERENCE_CODE")
    private String partnerRefferenceCode;

    @Column(name = "BRANCH_MANAGER_ID")
    private String branchManagerId;

    //COMMA SEPERATED
    @Column(name = "APPROVAL_AUTHORITY", nullable = false)
    private String approvalAuthority;

}
