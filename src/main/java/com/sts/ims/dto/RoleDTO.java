package com.sts.ims.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RoleDTO {

	@NotNull
	Long roleId;

	@NotEmpty
	String roleName;
}
