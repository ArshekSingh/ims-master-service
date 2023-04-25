package com.sas.ims.repository;

import com.sas.ims.entity.ProductGroup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductGroupRepository extends JpaRepository<ProductGroup, Long> {

    Optional<ProductGroup> findByOrgIdAndProductGroupId(Long orgId, Long groupId);

    Optional<List<ProductGroup>> findByOrgIdAndGroupCode(Long orgId, String groupCode, Pageable pageable);

    Optional<List<ProductGroup>> findByOrgIdAndStatusAndGroupType(Long orgId, String status, String groupType);

}
