package com.sas.ims.repository;

import com.sas.ims.entity.OrganisationHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganisationHierarchyRepository  extends JpaRepository<OrganisationHierarchy,Long> {


    Optional<List<OrganisationHierarchy>> findByOrgIdAndHierarchyTypeOrderByHierarchySequenceAsc(Long valueOf, String geo);

    List<OrganisationHierarchy> findByOrgIdAndHierarchyCodeAndHierarchyTypeAndStatus(Long orgId, String vendor, String geo, boolean b);
}