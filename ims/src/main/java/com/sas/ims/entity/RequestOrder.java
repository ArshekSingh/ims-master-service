package com.sas.ims.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "REQUEST_ORDER")
@Data
public class RequestOrder extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 4217773839649051188L;

    @Id
    @Column(name = "REQUEST_ORDER_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requestOrderId;

    @Column(name = "ORG_ID")
    private Long orgId;

    @Column(name = "LOAN_ID")
    private Long loanId;

    @Column(name = "LOAN_CODE")
    private String loanCode;

    @Column(name = "CLIENT_ID")
    private Long clientId;

    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "PRODUCT_CODE")
    private String productCode;

    @Column(name = "BRANCH_ID")
    private Integer branchId;

    @Column(name = "BRANCH_CODE")
    private String branchCode;

    @Column(name = "BRANCH_ADDRESS")
    private String branchAddress;

    @Column(name = "CUSTOMER_SUPPORT_NUMBER")
    private String customerSupportNumber;

    @Column(name = "REQUEST_ORDER_DATE")
    private LocalDateTime requestOrderDate;

    @Column(name = "DISBURSEMENT_DATE")
    private LocalDateTime disbursementDate;

    @Column(name = "CLIENT_NAME")
    private String clientName;

    @Column(name = "CLIENT_ADDRESS")
    private String clientAddress;

    @Column(name = "BILLING_ADDRESS")
    private String billingAddress;

    @Column(name = "SHIPPING_ADDRESS")
    private String shippingAddress;

    @Column(name = "CUSTOMER_PINCODE")
    private Integer customerPincode;

    @Column(name = "BRANCH_PINCODE")
    private Integer branchPincode;

    @Column(name = "BRANCH_PHONE")
    private String branchPhone;

    @Column(name = "BRANCH_NAME")
    private String branchName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "BATCH_ID")
    private String batchId;
}
