package com.sas.service;

import com.sas.dto.AccessGroupDTO;
import com.sas.dto.Response;
import com.sas.dto.UserAccessGroupDTO;
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
