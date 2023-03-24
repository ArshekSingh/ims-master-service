package com.sts.ims.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sts.ims.entity.ProductGroup;

@Repository
public interface ProductGroupRepository extends JpaRepository<ProductGroup, Long>{
	
	Optional<ProductGroup> findByProductGroupIdAndOrgId(Long groupId, Long orgId);
	
	Optional<List<ProductGroup>> findByOrgIdAndGroupCode(Long orgId, String groupCode,Pageable pageable);
	
	Optional<List<ProductGroup>> findByOrgIdAndActiveAndGroupType(Long orgId, String status,String groupType);

}
