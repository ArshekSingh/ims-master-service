package com.sas.ims.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferenceDetailDto {
    private String referenceDomain;
    private String keyValue;
    private String description;
    private String value1;
    private String value2;
    private String value3;
    private String value4;
    private Long orgId;
    private String insertedOn;
    private String insertedBy;
    private String updatedOn;
    private String updatedBy;
}