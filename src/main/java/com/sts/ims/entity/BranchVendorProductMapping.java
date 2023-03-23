package com.sts.ims.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "BRANCH_VENDOR_PRODUCT_MAPPING")
public class BranchVendorProductMapping implements Serializable{

	private static final long serialVersionUID = -598940486010829682L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BVP_ID")
    private String BVP_ID;

    @Column(name = "BVP_CODE")
    private String BVP_CODE;

    @Column(name = "ORG_ID")
    private Long ORG_ID;

    @Column(name = "BRANCH_ID")
    private Long BRANCH_ID;

    @Column(name = "VENDOR_ID")
    private Long VENDOR_ID;

    @Column(name = "PRODUCT_ID")
    private Long PRODUCT_ID;
}
