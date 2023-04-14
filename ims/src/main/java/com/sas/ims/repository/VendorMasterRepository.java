package com.sas.ims.repository;

import com.sas.ims.entity.VendorMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface VendorMasterRepository extends JpaRepository<VendorMaster, Long> {
    Page<VendorMaster> findByOrgId(Long orgId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update vendor_master set vendor_status='X' where org_id =:companyId and vendor_id =:vendorId",nativeQuery = true)
    void updateVendorDetails(Long companyId, Long vendorId);
}
