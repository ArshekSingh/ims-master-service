package com.sas.ims.repository;

import com.sas.ims.entity.AreaProductMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaProductMappingRepository extends JpaRepository<AreaProductMapping, Long> {


    @Query(value = "SELECT LPM.PRODUCT_ID,LPM.PRODUCT_NAME,LPM.PRODUCT_CODE FROM AREA_PRODUCT_MAPPING BPM INNER JOIN PRODUCT_MASTER LPM ON LPM.PRODUCT_ID=BPM.PRODUCT_ID WHERE BPM.ORG_ID=:companyId AND BPM.AREA_ID=:areaId", nativeQuery = true)
    List<Object[]> getProductsByOrgIdAndAreaId(Long companyId, Long areaId);

    List<AreaProductMapping> findByAreaProductMappingPK_AreaIdAndAreaProductMappingPK_OrgId(Long areaId, Long orgId);
}
