package com.sts.ims.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserAccessGroupDTO {

	@Valid
	@NotEmpty
	Set<Long> accessGroupIds = new HashSet<>();

	@NotNull
	Long userId;

}