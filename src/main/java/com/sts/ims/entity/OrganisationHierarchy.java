package com.sts.ims.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ORG_HIERARCHY")
@Data
public class OrganisationHierarchy extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8429414798376337020L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Long id;

    @Column(name = "ORG_ID")
    private Long orgId;

    @Column(name = "HIER_CODE")
    private String hierarchyCode;

    @Column(name = "HIER_NAME")
    private String hierarchyName;

    @Column(name = "HIER_TYPE")
    private String hierarchyType;

    @Column(name = "HIER_SEQUENCE")
    private Long hierarchySequence;

    @Column(name = "STATUS")
    private boolean status;

}












