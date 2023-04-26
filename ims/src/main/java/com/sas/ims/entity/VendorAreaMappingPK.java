package com.sas.ims.entity;

import javax.persistence.Column;
import java.io.Serializable;

public class VendorAreaMappingPK implements Serializable {
    @Column(name = "ORG_ID")
    private Long orgId;

    @Column(name = "AREA_ID")
    private Long areaId;

    @Column(name = "VENDOR_ID")
    private Long vendorId;
}
