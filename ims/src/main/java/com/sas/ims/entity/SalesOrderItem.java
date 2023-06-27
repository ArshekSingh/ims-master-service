package com.sas.ims.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "SALES_ORDER_ITEM")
@EqualsAndHashCode(callSuper = false)
public class SalesOrderItem extends BaseEntity implements Serializable {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORG_ID")
    private Long orgId;

    @Column(name = "SALES_ORDER_ID")
    private Long salesOrderId;

    @Column(name = "REQUEST_ORDER_ID")
    private Long requestOrderId;

    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "PRODUCT_QTY")
    private BigDecimal productQuantity;

    @Column(name = "PRODUCT_AMOUNT")
    private BigDecimal productAmount;
}