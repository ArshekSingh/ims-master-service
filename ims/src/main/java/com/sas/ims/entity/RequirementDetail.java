package com.sas.ims.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "REQUIREMENT_DETAIL")
@Data
public class RequirementDetail {
    private static final long serialVersionUID = 4217773839649051188L;

    @Id
    @Column(name = "REQUIREMENT_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requirementId;

    @Column(name = "ORG_ID")
    private Long orgId;

    @Column(name = "LOAN_ID")
    private Long loanId;

    @Column(name = "LOAN_CODE")
    private String loanCode;

    @Column(name = "CLIENT_ID")
    private Long clientId;

    @Column(name = "CLIENT_ADDRESS")
    private String clientAddress;

    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Column(name = "PRODUCT_CODE")
    private String productCode;

    @Column(name = "BRANCH_ID")
    private Long branchId;

    @Column(name = "BRANCH_CODE")
    private String branchCode;

    @Column(name = "BRANCH_ADDRESS")
    private String branchAddress;

    @Column(name = "CUSTOMER_SUPPORT_NUMBER")
    private String customerSupportNumber;


}
