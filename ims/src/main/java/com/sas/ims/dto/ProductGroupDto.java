package com.sas.ims.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductGroupDto extends BaseDto{
	
	private Long orgId;
	private Long partnerGroupId;
	private Long productGroupId;
	private String groupCode;
	private String groupName;
	private String status;
	private int startIndex;
	private int endIndex;
	private String groupType;
	
	
	

}
