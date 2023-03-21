package com.sts.ims.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class RoleGroupDTO {

	private String roleGroupName;
	
	@Valid
	@NotEmpty
	Set<RoleDTO> roles = new HashSet<>();
}
