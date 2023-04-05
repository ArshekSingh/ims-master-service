package com.sts.ims.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductGroupDto extends BaseDto{
	
	private Long orgId;
	private Long parentGroupId;
	private Long productGroupId;
	private String groupCode;
	private String groupName;
	private String status;
	private int startIndex;
	private int endIndex;
	private String groupType;
	
	
	

}
