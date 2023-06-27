package com.sas.ims.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PACKAGE_DETAIL")
@Data
public class PackageDetail extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4354531309172051123L;

    @Id
    @Column(name = "PACKAGE_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long packageId;

    @Column(name = "PACKAGE_DATE")
    private LocalDateTime packageDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PACKAGE_ITEM")
    private String packageItem;

    @Column(name = "REQUEST_ID")
    private String requestId;

    @Column(name = "SALES_ORDER_ID")
    private String salesOrderId;

}
