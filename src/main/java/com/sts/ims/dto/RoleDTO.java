package com.sts.ims.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RoleDTO {

	@NotNull
	Long roleId;

	@NotEmpty
	String roleName;
}
