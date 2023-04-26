package com.sas.ims.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class VendorProductMappingPK implements Serializable {

    @Column(name = "ORG_ID")
    private Long orgId;

    @Column(name = "VENDOR_ID")
    private Long vendorId;

    @Column(name = "PRODUCT_ID")
    private Long productId;
}
