package com.sas.ims.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class PincodeMasterPK implements Serializable {

    @Column(name = "COUNTRY_ID")
    private Integer countryId;

    @Column(name = "PINCODE")
    private Integer pincode;
}