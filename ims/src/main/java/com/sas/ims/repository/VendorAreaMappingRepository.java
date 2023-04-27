package com.sas.ims.repository;

import com.sas.ims.entity.VendorAreaMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VendorAreaMappingRepository extends JpaRepository<VendorAreaMapping, Long> {

    @Query(value = "SELECT LPM.AREA_ID,LPM.AREA_NAME,LPM.AREA_CODE FROM VENDOR_AREA_MAPPING BPM INNER JOIN AREA_MASTER LPM ON LPM.AREA_ID=BPM.AREA_ID WHERE BPM.ORG_ID=:companyId AND BPM.VENDOR_ID=:vendorId", nativeQuery = true)
    List<Object[]> getAreasByOrgIdAndVendorId(Long companyId, Long vendorId);

    List<VendorAreaMapping> findByVendorAreaMappingPK_VendorIdAndVendorAreaMappingPK_OrgId(Long vendorId, Long orgId);
}
