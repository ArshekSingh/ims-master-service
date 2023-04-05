package com.sts.ims.repository;

import com.sts.ims.entity.OrganisationHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrganisationHierarchyRepository extends JpaRepository<OrganisationHierarchy, Long> {
    List<OrganisationHierarchy> findByOrgIdAndHierarchyCodeAndHierarchyTypeAndStatus(Long orgId, String hierarchyCode, String hierarchyType, boolean status);
}
