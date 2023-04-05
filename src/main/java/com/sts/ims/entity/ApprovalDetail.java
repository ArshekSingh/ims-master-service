package com.sts.ims.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "APPROVAL_DETAIL")
@Getter
@Setter
public class ApprovalDetail extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "APPROVAL_ID")
    private Long approvalId;

    @Column(name = "ORG_ID")
    private Long orgId;

    @Column(name = "APPROVAL_TYPE")
    private String approvalType;

    @Column(name = "ENTITY_TYPE")
    private String entityType;

    @Column(name = "ENTITY_ID")
    private Long entityId;

    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;
}
