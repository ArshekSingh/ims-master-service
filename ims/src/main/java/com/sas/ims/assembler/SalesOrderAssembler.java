package com.sas.ims.assembler;

import com.sas.ims.dto.SalesOrderItemDto;
import com.sas.ims.response.RequestOrderXlsResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SalesOrderAssembler {
    public List<List<String>> prepareSalesOrderData(List<SalesOrderItemDto> data) {
        return data.stream().map(this::constructData).collect(Collectors.toList());
    }

    private List<String> constructData(SalesOrderItemDto uploader) {
        List<String> data = new ArrayList<>();
        data.add(StringUtils.hasText(uploader.getSalesOrderId()) ? uploader.getSalesOrderId() : "");
        data.add(StringUtils.hasText(uploader.getOrderDate()) ? uploader.getOrderDate() : "");
        data.add(StringUtils.hasText(uploader.getExpectedShipment()) ? uploader.getExpectedShipment() : "");
        data.add(StringUtils.hasText(uploader.getSalesOrderNumber()) ? uploader.getSalesOrderNumber() : "");
        data.add(StringUtils.hasText(uploader.getStatus()) ? uploader.getStatus() : "");
        data.add(StringUtils.hasText(uploader.getCustomStatus()) ? uploader.getCustomStatus() : "");
        data.add(uploader.getCustomerId() != null ? String.valueOf(uploader.getCustomerId()) : "");
        data.add(StringUtils.hasText(uploader.getSalesChannel()) ? uploader.getSalesChannel() : "");
        data.add(StringUtils.hasText(uploader.getCustomerName()) ? uploader.getCustomerName() : "");
        data.add(StringUtils.hasText(uploader.getPlaceOfSupply()) ? uploader.getPlaceOfSupply() : "");
        data.add(StringUtils.hasText(uploader.getGstTreatment()) ? uploader.getGstTreatment() : "");
        data.add(StringUtils.hasText(uploader.getGstIn()) ? uploader.getGstIn() : "");
        data.add(uploader.getIsInclusiveTax() != null ? String.valueOf(uploader.getIsInclusiveTax()) : "");
        data.add(StringUtils.hasText(uploader.getPurchaseOrder()) ? uploader.getPurchaseOrder() : "");
        data.add(StringUtils.hasText(uploader.getTemplateName()) ? uploader.getTemplateName() : "");
        data.add(StringUtils.hasText(uploader.getCurrencyCode()) ? uploader.getCurrencyCode() : "");
        data.add(uploader.getExchangeRate() != null ? String.valueOf(uploader.getExchangeRate()) : "");
        data.add(StringUtils.hasText(uploader.getDiscountType()) ? uploader.getDiscountType() : "");
        data.add(StringUtils.hasText(uploader.getIsDiscountBeforeTax()) ? uploader.getIsDiscountBeforeTax() : "");
        data.add(uploader.getEntityDiscountAmount() != null ? String.valueOf(uploader.getEntityDiscountAmount()) : "");
        data.add(uploader.getEntityDiscountPercentage() != null ? String.valueOf(uploader.getEntityDiscountPercentage()) : "");
        data.add(StringUtils.hasText(uploader.getItemName()) ? uploader.getItemName() : "");
        data.add(uploader.getProductId() != null ? String.valueOf(uploader.getProductId()) : "");
        data.add(StringUtils.hasText(uploader.getSku()) ? uploader.getSku() : "");
        data.add(StringUtils.hasText(uploader.getUpc()) ? uploader.getUpc() : "");
        data.add(StringUtils.hasText(uploader.getMpn()) ? uploader.getMpn() : "");
        data.add(StringUtils.hasText(uploader.getEan()) ? uploader.getEan() : "");
        data.add(StringUtils.hasText(uploader.getIsbn()) ? uploader.getIsbn() : "");
        data.add(StringUtils.hasText(uploader.getAccount()) ? uploader.getAccount() : "");
        data.add(StringUtils.hasText(uploader.getItemDescription()) ? uploader.getItemDescription() : "");
        data.add(uploader.getQuantityOrdered() != null ? String.valueOf(uploader.getQuantityOrdered()) : "");
        data.add(uploader.getQuantityInvoiced() != null ? String.valueOf(uploader.getQuantityInvoiced()) : "");
        data.add(uploader.getQuantityPacked() != null ? String.valueOf(uploader.getQuantityPacked()) : "");
        data.add(uploader.getQuantityCancelled() != null ? String.valueOf(uploader.getQuantityCancelled()) : "");
        data.add(StringUtils.hasText(uploader.getUsageUnit()) ? uploader.getUsageUnit() : "");
        data.add(StringUtils.hasText(uploader.getWarehouseName()) ? uploader.getWarehouseName() : "");
        data.add(uploader.getItemPrice() != null ? String.valueOf(uploader.getItemPrice()) : "");
        data.add(uploader.getDiscount() != null ? String.valueOf(uploader.getDiscount()) : "");
        data.add(uploader.getDiscountAmount() != null ? String.valueOf(uploader.getDiscountAmount()) : "");
        data.add(StringUtils.hasText(uploader.getHsnOrSac()) ? uploader.getHsnOrSac() : "");
        data.add(StringUtils.hasText(uploader.getSupplyType()) ? uploader.getSupplyType() : "");
        data.add(StringUtils.hasText(uploader.getTaxId()) ? uploader.getTaxId() : "");
        data.add(uploader.getItemTax() != null ? String.valueOf(uploader.getItemTax()) : "");
        data.add(uploader.getItemTaxPercent() != null ? String.valueOf(uploader.getItemTaxPercent()) : "");
        data.add(uploader.getItemTaxAmount() != null ? String.valueOf(uploader.getItemTaxAmount()) : "");
        data.add(StringUtils.hasText(uploader.getItemTaxType()) ? uploader.getItemTaxType() : "");
        data.add(StringUtils.hasText(uploader.getReverseChargeTaxName()) ? uploader.getReverseChargeTaxName() : "");
        data.add(uploader.getReverseChargeTaxRate() != null ? String.valueOf(uploader.getReverseChargeTaxRate()) : "");
        data.add(StringUtils.hasText(uploader.getReverseChargeTaxType()) ? uploader.getReverseChargeTaxType() : "");
        data.add(StringUtils.hasText(uploader.getItemTaxAuthority()) ? uploader.getItemTaxAuthority() : "");
        data.add(StringUtils.hasText(uploader.getItemTaxExemptionReason()) ? uploader.getItemTaxExemptionReason() : "");
        data.add(StringUtils.hasText(uploader.getItemType()) ? uploader.getItemType() : "");
        data.add(uploader.getItemTotal() != null ? String.valueOf(uploader.getItemTotal()) : "");
        data.add(uploader.getSubTotal() != null ? String.valueOf(uploader.getSubTotal()) : "");
        data.add(uploader.getTotal() != null ? String.valueOf(uploader.getTotal()) : "");
        data.add(uploader.getShippingCharge() != null ? String.valueOf(uploader.getShippingCharge()) : "");
        data.add(StringUtils.hasText(uploader.getShippingChargeTaxId()) ? uploader.getShippingChargeTaxId() : "");
        data.add(uploader.getShippingChargeTaxAmount() != null ? String.valueOf(uploader.getShippingChargeTaxAmount()) : "");
        data.add(StringUtils.hasText(uploader.getShippingChargeTaxName()) ? uploader.getShippingChargeTaxName() : "");
        data.add(uploader.getShippingChargeTaxPercent() != null ? String.valueOf(uploader.getShippingChargeTaxPercent()) : "");
        data.add(StringUtils.hasText(uploader.getShippingChargeTaxType()) ? uploader.getShippingChargeTaxType() : "");
        data.add(StringUtils.hasText(uploader.getShippingChargeTaxExemptionCode()) ? uploader.getShippingChargeTaxExemptionCode() : "");
        data.add(StringUtils.hasText(uploader.getShippingChargeSacCode()) ? uploader.getShippingChargeSacCode() : "");
        data.add(uploader.getAdjustment() != null ? String.valueOf(uploader.getAdjustment()) : "");
        data.add(StringUtils.hasText(uploader.getAdjustmentDescription()) ? uploader.getAdjustmentDescription() : "");
        data.add(StringUtils.hasText(uploader.getSalesPerson()) ? uploader.getSalesPerson() : "");
        data.add(StringUtils.hasText(uploader.getPaymentTerms()) ? uploader.getPaymentTerms() : "");
        data.add(StringUtils.hasText(uploader.getPaymentTermsLabel()) ? uploader.getPaymentTermsLabel() : "");
        data.add(StringUtils.hasText(uploader.getNotes()) ? uploader.getNotes() : "");
        data.add(StringUtils.hasText(uploader.getTermsAndConditions()) ? uploader.getTermsAndConditions() : "");
        data.add(StringUtils.hasText(uploader.getDeliveryMethod()) ? uploader.getDeliveryMethod() : "");
        data.add(StringUtils.hasText(uploader.getSource()) ? uploader.getSource() : "");
        data.add(StringUtils.hasText(uploader.getBillingAddress()) ? uploader.getBillingAddress() : "");
        data.add(StringUtils.hasText(uploader.getBillingCity()) ? uploader.getBillingCity() : "");
        data.add(StringUtils.hasText(uploader.getBillingState()) ? uploader.getBillingState() : "");
        data.add(StringUtils.hasText(uploader.getBillingCountry()) ? uploader.getBillingCountry() : "");
        data.add(StringUtils.hasText(uploader.getBillingCode()) ? uploader.getBillingCode() : "");
        data.add(StringUtils.hasText(uploader.getBillingFax()) ? uploader.getBillingFax() : "");
        data.add(StringUtils.hasText(uploader.getBillingPhone()) ? uploader.getBillingPhone() : "");
        data.add(StringUtils.hasText(uploader.getShippingAddress()) ? uploader.getShippingAddress() : "");
        data.add(StringUtils.hasText(uploader.getShippingCity()) ? uploader.getShippingCity() : "");
        data.add(StringUtils.hasText(uploader.getShippingState()) ? uploader.getShippingState() : "");
        data.add(StringUtils.hasText(uploader.getShippingCountry()) ? uploader.getShippingCountry() : "");
        data.add(StringUtils.hasText(uploader.getShippingCode()) ? uploader.getShippingCode() : "");
        data.add(StringUtils.hasText(uploader.getShippingFax()) ? uploader.getShippingFax() : "");
        data.add(StringUtils.hasText(uploader.getShippingPhone()) ? uploader.getShippingPhone() : "");
        data.add(StringUtils.hasText(uploader.getCfBranchName()) ? uploader.getCfBranchName() : "");
        data.add(StringUtils.hasText(uploader.getCfClientId()) ? uploader.getCfClientId() : "");
        data.add(StringUtils.hasText(uploader.getCfCustomerSupport()) ? uploader.getCfCustomerSupport() : "");
        return data;
    }

}
