package com.sas.ims.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class AreaProductMappingPK implements Serializable {

    @Column(name = "ORG_ID")
    private Long orgId;

    @Column(name = "AREA_ID")
    private Long areaId;

    @Column(name = "PRODUCT_ID")
    private Long productId;
}
