package com.sas.ims.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "AREA_VENDOR_PRODUCT_MAPPING")
public class AreaVendorProductMapping implements Serializable{

	private static final long serialVersionUID = -598940486010829682L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BVP_ID")
    private String BVP_ID;

    @Column(name = "BVP_CODE")
    private String BVP_CODE;

    @Column(name = "ORG_ID")
    private Long ORG_ID;

    @Column(name = "AREA_ID")
    private Long AREA_ID;

    @Column(name = "VENDOR_ID")
    private Long VENDOR_ID;

    @Column(name = "PRODUCT_ID")
    private Long PRODUCT_ID;
}
