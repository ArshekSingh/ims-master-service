package com.sts.ims.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sts.ims.entity.ProductMaster;

@Repository
public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long>{
	
	Optional<ProductMaster> findByProductIdAndOrgId(Long productId, Long orgId);
	
	Optional<List<ProductMaster>> findByOrgIdAndProductCode(Long orgId, String productCode,Pageable pageable);

}
