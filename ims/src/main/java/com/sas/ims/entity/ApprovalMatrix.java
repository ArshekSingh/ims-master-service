package com.sas.ims.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "APPROVAL_MATRIX")
@Getter
@Setter
public class ApprovalMatrix extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "approval_type")
    private String approvalType;

    @Column(name = "approval_entity")
    private Long approvalEntity;

    @Column(name = "min_amt")
    private Long minAmount;

    @Column(name = "max_amt")
    private Long maxAmount;

    @Column(name = "min_qty")
    private Long minQuantity;

    @Column(name = "max_qty")
    private Long maxQuantity;

    @Column(name = "status")
    private Long status;
}
