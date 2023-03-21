package com.sas.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductMasterDto extends BaseDto{
	
	private Long orgId;
	private Long productGroupId;
	private Long productId;
	private String productCode;
	private String productName;
	private BigDecimal amount;
	private BigDecimal quantity;
	private String status;
	private Long vendorId;
	private String partnerRefferenceCode;
	private String productIdentifierCode;
	private String productOpeningDate;
	private String productClosingDate;
	
	
	

}
