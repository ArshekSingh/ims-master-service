package com.sas.ims.constant;

public interface Constant {

    String DEFAULT_TENANT = "main_db";
    String NOT_FOUND = "Record not found";
    String SUCCESS = "Transaction completed successfully";

    String AUTHENTICATION_FAILED = "Authentication Failed";
    String INVALID_TENANT_IDENTIFIER = "Invalid tenant identifier";
    String INVALID_USERNAME_PASSWORD = "Invalid username/password";
    String INVALID_REQUEST="Invalid Request";

    String ACTIVE_STATUS="A";
    String IN_ACTIVE_STATUS="X";
    String PARTNER_GROUP_TYPE ="P";

    public static final String REQUEST_ORDER_ERROR_HEADER = "loanId, loanCode, clientId, productId, productCode, branchId, branchCode, branchAddress, customerSupportNumber, batchId, errorMessage";

    public static final String SALES_ORDER_HEADER = "SalesOrder ID, Order Date, Expected Shipment Date, SalesOrder Number, Status, Custom Status, Customer ID, Sales Channel, Customer Name, Place of Supply, GST Treatment, GST Identification Number (GSTIN), Is Inclusive Tax, PurchaseOrder, Template Name, Currency Code, Exchange Rate, Discount Type, Is Discount Before Tax, Entity Discount Amount, Entity Discount Percent, Item Name, Product ID, SKU, UPC, MPN, EAN, ISBN, Account, Item Desc, QuantityOrdered, QuantityInvoiced, QuantityPacked, QuantityCancelled, Usage unit, Warehouse Name, Item Price, Discount, Discount Amount, HSN/SAC, Supply Type, Tax ID, Item Tax, Item Tax %, Item Tax Amount, Item Tax Type, Reverse Charge Tax Name, Reverse Charge Tax Rate, Reverse Charge Tax Type, Item Tax Authority, Item Tax Exemption Reason, Item Type, Item Total, SubTotal, Total, Shipping Charge, Shipping Charge Tax ID, Shipping Charge Tax Amount, Shipping Charge Tax Name, Shipping Charge Tax %, Shipping Charge Tax Type, Shipping Charge Tax Exemption Code, Shipping Charge SAC Code, Adjustment, Adjustment Description, Sales Person, Payment Terms, Payment Terms Label, Notes, Terms & Conditions, Delivery Method, Source, Billing Address, Billing City, Billing State, Billing Country, Billing Code, Billing Fax, Billing Phone, Shipping Address, Shipping City, Shipping State, Shipping Country, Shipping Code, Shipping Fax, Shipping Phone, CF.Branch Name, CF.Client Id, CF.Customer Support";

    public static final String REFERENCE_DETAIL_H = "REFERENCE_DETAIL_H";

}
