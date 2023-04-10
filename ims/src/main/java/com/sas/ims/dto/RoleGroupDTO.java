package com.sas.ims.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
public class RoleGroupDTO {

	private String roleGroupName;
	
	@Valid
	@NotEmpty
	Set<RoleDTO> roles = new HashSet<>();
}
