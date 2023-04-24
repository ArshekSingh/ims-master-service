package com.sas.ims.repository;

import com.sas.ims.entity.BranchProductMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchProductMappingRepository extends JpaRepository<BranchProductMapping, Long> {


    @Query(value = "SELECT LPM.PRODUCT_ID,LPM.PRODUCT_NAME,LPM.PRODUCT_CODE FROM BRANCH_PRODUCT_MAPPING BPM INNER JOIN PRODUCT_MASTER LPM ON LPM.PRODUCT_ID=BPM.PRODUCT_ID WHERE BPM.ORG_ID=:companyId AND BPM.BRANCH_ID=:branchId", nativeQuery = true)
    List<Object[]> getProductsByOrgIdAndBranchId(Long companyId, Long branchId);
}
