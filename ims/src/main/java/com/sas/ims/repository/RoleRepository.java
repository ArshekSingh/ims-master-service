package com.sas.ims.repository;

import com.sas.ims.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByRoleName(String role);

	@Query(nativeQuery = true, value = "SELECT role FROM role r INNER JOIN access_group_role agr ON agr.role_id=r.role_id INNER JOIN user_access_group uag ON uag.access_group_id=agr.access_group_id WHERE uag.user_id=?1")
	Set<String> findAllRolesForUser(Long userId);

	@Query(nativeQuery = true, value = "SELECT DISTINCT(role_group) FROM role r")
	Set<String> findAllRolesGroups();
	
	Set<Role> findByRoleGroup(String roleGroup);

}
