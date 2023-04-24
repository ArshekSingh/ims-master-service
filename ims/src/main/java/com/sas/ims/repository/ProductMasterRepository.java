package com.sas.ims.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.sas.ims.entity.ProductMaster;

@Repository
public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long> {
	
	Optional<ProductMaster> findByOrgIdAndProductId(Long orgId, Long productId);
	
	Page<ProductMaster> findByOrgIdAndProductCode(Long orgId, String productCode, Pageable pageable);

	@Query(value = "SELECT LPM.PRODUCT_ID, LPM.PRODUCT_NAME, LPM.PRODUCT_CODE FROM PRODUCT_MASTER LPM WHERE LPM.ORG_ID =:companyId AND LPM.STATUS ='A'", nativeQuery = true)
	List<Object[]> findAllProductDetailByOrgId(Long companyId);

	@Query(value = "SELECT LPM.PRODUCT_ID, LPM.PRODUCT_NAME, LPM.PRODUCT_CODE FROM PRODUCT_MASTER LPM WHERE LPM.ORG_ID =:companyId AND LPM.PRODUCT_ID NOT IN (:productList) AND LPM.STATUS ='A'", nativeQuery = true)
	List<Object[]> findAllProductsByOrganizationIdNotIn(Long companyId, List<Integer> productList);
}
