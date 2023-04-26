package com.sas.ims.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VENDOR_AREA_MAPPING")
public class VendorAreaMapping extends BaseEntity {

    @EmbeddedId
    private VendorAreaMappingPK vendorAreaMappingPK;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AREA_ID", insertable = false, updatable = false)
    private AreaMaster areaMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VENDOR_ID", insertable = false, updatable = false)
    private VendorMaster vendorMaster;

}
