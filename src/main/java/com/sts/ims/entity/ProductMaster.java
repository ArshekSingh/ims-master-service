package com.sts.ims.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductMaster extends BaseEntity{
	
	@Id
	@Column(name = "PRODUCT_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long productId ;  
	
	@Column(name = "ORG_ID")           
	private Long orgId;   
	
	@Column(name = "PRODUCT_GROUP_ID")
	private Long productGroupId;        
	  
	@Column(name = "PRODUCT_CODE")
	private String productCode; 
	
	@Column(name = "PRODUCT_NAME")
	private String productName;  
	
	@Column(name = "AMOUNT")
	private BigDecimal amount;      
	
	@Column(name = "QUANTITY")
	private BigDecimal quantity;  
	
	@Column(name = "STATUS")
	private String status;       
	
	@Column(name = "VENDOR_ID")
	private Long vendorId; 
	
	@Column(name = "PARTNER_REFFERENCE_CODE")
	private String partnerRefferenceCode;
	
	@Column(name = "PRODUCT_IDENTIFIER_CODE")
	private String productIdentifierCode;
	
	@Column(name = "PRODUCT_OPENING_DATE")
	private LocalDateTime productOpeningDate;   
	
	@Column(name = "PRODUCT_CLOSING_DATE")
	private LocalDateTime productClosingDate;

}
