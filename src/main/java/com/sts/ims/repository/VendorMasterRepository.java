package com.sts.ims.repository;

import com.sts.ims.entity.VendorMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorMasterRepository extends JpaRepository<VendorMaster, Long> {
    Page<VendorMaster> findByOrgId(Long orgId, Pageable pageable);
}
