package com.sts.ims.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
public class AccessGroupDTO {

	@Valid
	@NotEmpty
	Set<RoleDTO> roles = new HashSet<>();

	Long accessGroupId;

	@NotEmpty
	String accessGroupName;

	int active;

	HashMap<String, Set<RoleDTO>> groupRoles = new HashMap<>();

}