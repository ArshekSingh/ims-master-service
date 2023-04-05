package com.sts.ims.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "BRANCH_MASTER")
public class BranchMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 9182562580678493055L;

    @Id
    @Column(name = "ORG_ID")
    private Long ORG_ID;

    @Column(name = "BRANCH_ID")
    private Long BRANCH_ID;

    @Column(name = "BRANCH_CODE")
    private String BRANCH_CODE;

    @Column(name = "BRANCH_NAME")
    private String BRANCH_NAME;

    @Column(name = "BRANCH_TYPE")
    private String BRANCH_TYPE;

    @Column(name = "PARENT_ID")
    private Long PARENT_ID;

    @Column(name = "BRANCH_OPENING_DATE")
    private LocalDateTime BRANCH_OPENING_DATE;

    @Column(name = "BRANCH_CLOSING_DATE")
    private LocalDateTime BRANCH_CLOSING_DATE;

    @Column(name = "STATUS")
    private String STATUS;

    @Column(name = "ADDRESS", nullable = false)
    private String ADDRESS;

    @Column(name = "PINCODE")
    private Integer PINCODE;

    @Column(name = "PHONE_NUMBER")
    private Long PHONE_NUMBER;

    @Column(name = "EMAIL_ID")
    private String EMAIL_ID;

    @Column(name = "PARTNER_REFFERENCE_CODE")
    private String PARTNER_REFFERENCE_CODE;

    @Column(name = "BRANCH_MANAGER_ID")
    private String BRANCH_MANAGER_ID;

    //COMMA SEPERATED
    @Column(name = "APPROVAL_AUTHORITY", nullable = false)
    private String APPROVAL_AUTHORITY;

}