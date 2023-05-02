package com.sas.ims.assembler;

import com.sas.ims.dto.RoleDTO;
import com.sas.ims.entity.Role;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleAssembler {

    public List<RoleDTO> entityToDtoList(List<Role> roles) {
        if (CollectionUtils.isEmpty(roles))
            return Collections.emptyList();
        return roles.parallelStream().map(this::entityToDto).collect(Collectors.toList());
    }

    public RoleDTO entityToDto(Role request) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleId(request.getId());
        roleDTO.setRoleName(request.getRoleName());
        roleDTO.setRoleGroup(request.getRoleGroup());
        return roleDTO;

    }
}
