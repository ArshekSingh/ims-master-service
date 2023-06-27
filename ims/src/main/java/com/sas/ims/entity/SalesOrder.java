package com.sas.ims.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "SALES_ORDER")
@EqualsAndHashCode(callSuper = false)
public class SalesOrder extends BaseEntity implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "SALES_ORDER_ID")
    private String salesOrderId;

    @Column(name = "SALES_ORDER_NUMBER")
    private String salesOrderNumber;

    @Column(name = "REQUEST_ID")
    private String requestId;

    @Column(name = "ORG_ID")
    private Long orgId;

    @Column(name = "PKT_REF")
    private Long pktReference;

    @Column(name = "BATCH_ID")
    private String batchId;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "SALES_ORDER_TYPE")
    private String salesOrderType;
}