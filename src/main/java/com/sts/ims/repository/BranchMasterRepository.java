package com.sts.ims.repository;

import com.sts.ims.entity.BranchMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface BranchMasterRepository extends JpaRepository<BranchMaster, Long> {



    Optional<BranchMaster> findByBranchIdAndOrgId(Long branchId, Long orgId);




    Page<BranchMaster> findAll(Specification<BranchMaster> spec, Pageable pageable);

    boolean existsByBranchCode(String branchCode);
}
