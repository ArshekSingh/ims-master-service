package com.sas.ims.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class ReferenceDetailPK implements Serializable {

    @Column(name = "REFERENCE_DOMAIN")
    private String referenceDomain;

    @Column(name = "KEY_VALUE")
    private String keyValue;
}
