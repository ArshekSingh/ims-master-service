package com.sts.ims.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import com.sts.ims.dto.RoleDTO;

import lombok.Data;

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