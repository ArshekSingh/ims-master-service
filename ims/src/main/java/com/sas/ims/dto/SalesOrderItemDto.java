package com.sas.ims.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesOrderItemDto {
    private String salesOrderId;
    private String orderDate;
    private String expectedShipment;
    private String salesOrderNumber;
    private String status;
    private String customStatus;
    private Long customerId;
    private String salesChannel;
    private String customerName;
    private String placeOfSupply;
    private String gstTreatment;
    private String gstIn;
    private Boolean isInclusiveTax;
    private String purchaseOrder;
    private String templateName;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private String discountType;
    private String isDiscountBeforeTax;
    private BigDecimal entityDiscountAmount;
    private BigDecimal entityDiscountPercentage;
    private String itemName;
    private Long productId;
    private String sku;
    private String upc;
    private String mpn;
    private String ean;
    private String isbn;
    private String account;
    private String itemDescription;
    private Integer quantityOrdered;
    private Integer quantityInvoiced;
    private Integer quantityPacked;
    private Integer quantityCancelled;
    private String usageUnit;
    private String warehouseName;
    private BigDecimal itemPrice;
    private BigDecimal discount;
    private BigDecimal discountAmount;
    private String hsnOrSac;
    private String supplyType;
    private String taxId;
    private BigDecimal itemTax;
    private BigDecimal itemTaxPercent;
    private BigDecimal itemTaxAmount;
    private String itemTaxType;
    private String reverseChargeTaxName;
    private BigDecimal reverseChargeTaxRate;
    private String reverseChargeTaxType;
    private String itemTaxAuthority;
    private String itemTaxExemptionReason;
    private String itemType;
    private BigDecimal itemTotal;
    private BigDecimal subTotal;
    private BigDecimal total;
    private BigDecimal shippingCharge;
    private String shippingChargeTaxId;
    private BigDecimal shippingChargeTaxAmount;
    private String shippingChargeTaxName;
    private BigDecimal shippingChargeTaxPercent;
    private String shippingChargeTaxType;
    private String shippingChargeTaxExemptionCode;
    private String shippingChargeSacCode;
    private BigDecimal adjustment;
    private String adjustmentDescription;
    private String salesPerson;
    private String paymentTerms;
    private String paymentTermsLabel;
    private String notes;
    private String termsAndConditions;
    private String deliveryMethod;
    private String source;
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingCountry;
    private String billingCode;
    private String billingFax;
    private String billingPhone;
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingCountry;
    private String shippingCode;
    private String shippingFax;
    private String shippingPhone;
    private String cfBranchName;
    private String cfClientId;
    private String cfCustomerSupport;

}
