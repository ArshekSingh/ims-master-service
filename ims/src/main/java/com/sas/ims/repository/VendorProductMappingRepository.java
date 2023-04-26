package com.sas.ims.repository;

import com.sas.ims.entity.VendorProductMapping;
import com.sas.ims.entity.VendorProductMappingPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorProductMappingRepository extends JpaRepository<VendorProductMapping, Long> {

    @Query(value = "SELECT LPM.PRODUCT_ID,LPM.PRODUCT_NAME,LPM.PRODUCT_CODE FROM VENDOR_PRODUCT_MAPPING VPM INNER JOIN PRODUCT_MASTER LPM ON LPM.PRODUCT_ID=VPM.PRODUCT_ID WHERE VPM.ORG_ID=:companyId AND VPM.VENDOR_ID=:vendorId", nativeQuery = true)
    List<Object[]> getProductsByOrgIdAndVendorId(Long companyId, Long vendorId);

    List<VendorProductMapping> findByVendorProductMappingPK_VendorIdAndVendorProductMappingPK_OrgId(Long vendorId, Long companyId);
}

