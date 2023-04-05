package com.sts.ims.controller;

import com.sts.ims.dto.AccessGroupDTO;
import com.sts.ims.dto.UserAccessGroupDTO;
import com.sts.ims.response.Response;
import com.sts.ims.service.AccessGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AccessGroupController {

	@Autowired
	AccessGroupService accessGroupService;

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/access_group/{pageNo}/{pageSize}")
	public Response accessGroups(@PathVariable(value = "pageNo") Integer pageNo,
								 @PathVariable(value = "pageSize") Integer pageSize) {
		if (pageSize > 0)
			return accessGroupService.findAll(pageNo, pageSize);
		else
			return Response.builder().code(HttpStatus.BAD_REQUEST.value()).message("Page Size cannot be zero")
					.status(HttpStatus.BAD_REQUEST).build();
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PostMapping("/access_group")
	public Response accessGroups(@Valid @RequestBody AccessGroupDTO accessGroupDTO) {

		return accessGroupService.addNewAccessGroup(accessGroupDTO);

	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PutMapping("/access_group")
	public Response accessGroupsUpdate(@Valid @RequestBody AccessGroupDTO accessGroupDTO) {

		return accessGroupService.updateAccessGroup(accessGroupDTO);

	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PutMapping("/user_access_group")
	public Response userAccessGroupsUpdate(@Valid @RequestBody UserAccessGroupDTO userAccessGroupDTO) {

		return accessGroupService.updateUserAccessGroup(userAccessGroupDTO);

	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/user_access_group/{userId}")
	public Response userAccessGroups(@PathVariable Long userId) {

		return accessGroupService.getUserAccessGroups(userId);

	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/role_groups")
	public Response roleGroups() {

		return accessGroupService.getRoleGroups();

	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/access_group_role/{accessGroupId}")
	public Response allRolesForAccessGroup(@PathVariable Long accessGroupId) {

		return accessGroupService.getAllRolesForAccessGroup(accessGroupId);

	}

}