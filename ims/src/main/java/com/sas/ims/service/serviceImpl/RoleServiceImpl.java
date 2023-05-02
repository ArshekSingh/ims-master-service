package com.sas.ims.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sas.ims.assembler.RoleAssembler;
import com.sas.ims.constant.Constant;
import com.sas.ims.dto.RoleDTO;
import com.sas.ims.entity.Role;
import com.sas.ims.repository.RoleRepository;
import com.sas.ims.service.RoleService;
import com.sas.tokenlib.response.Response;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService, Constant {

	private final RoleAssembler roleAssembler;
	private final RoleRepository roleRepository;

	@Override
	public Response getRoles() {
		log.info("Request received to fetch roles");
		List<Role> roles = roleRepository.findAll();
		if (CollectionUtils.isEmpty(roles))
			return new Response(NOT_FOUND, HttpStatus.NOT_FOUND);
		return new Response(SUCCESS,
				roleAssembler.entityToDtoList(roles).stream().collect(Collectors.groupingBy(RoleDTO::getRoleGroup)),
				HttpStatus.OK);
	}
}
