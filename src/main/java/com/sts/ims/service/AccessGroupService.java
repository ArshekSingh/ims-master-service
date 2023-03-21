package com.sts.ims.service;

import com.sts.ims.dto.AccessGroupDTO;
import com.sts.ims.dto.UserAccessGroupDTO;
import com.sts.ims.response.Response;

import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public interface AccessGroupService {

	Response findAll(int pageNo,int pageSize);
	
	Response addNewAccessGroup(AccessGroupDTO dto);
	
	Response updateAccessGroup(AccessGroupDTO dto);

	Response updateUserAccessGroup(@Valid UserAccessGroupDTO userAccessGroupDTO);
	
	Response getUserAccessGroups(Long userId);
	
	Response getRoleGroups();
	
	Response getAllRolesForAccessGroup(Long accessGroupId);
}
