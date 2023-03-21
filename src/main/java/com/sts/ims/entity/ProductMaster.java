package com.sts.ims.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCT_MASTER")
public class ProductMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 5261359491752386589L;

    @Column(name = "ORG_ID")
    private Long ORG_ID;

    @Column(name = "PRODUCT_GROUP_ID")
    private Long PRODUCT_GROUP_ID;

    @Id
    @Column(name = "PRODUCT_ID")
    private Long PRODUCT_ID;

    @Column(name = "PRODUCT_CODE")
    private String PRODUCT_CODE;

    @Column(name = "PRODUCT_NAME")
    private String PRODUCT_NAME;

    @Column(name = "AMOUNT")
    private BigDecimal AMOUNT;

    @Column(name = "QUANTITY")
    private BigDecimal QUANTITY;

    @Column(name = "STATUS")
    private String STATUS;

    @Column(name = "VENDOR_ID")
    private String VENDOR_ID;

    @Column(name = "PARTNER_REFFERENCE_CODE")
    private String PARTNER_REFFERENCE_CODE;

    @Column(name = "PRODUCT_IDENTIFIER_CODE")
    private String PRODUCT_IDENTIFIER_CODE;

    @Column(name = "")
    private LocalDateTime PRODUCT_OPENING_DATE;

    @Column(name = "")
    private LocalDateTime PRODUCT_CLOSING_DATE;

}
