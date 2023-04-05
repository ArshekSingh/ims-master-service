package com.sts.ims.repository;

import com.sts.ims.entity.BranchMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchMasterRepository extends JpaRepository<BranchMaster, Integer> {

}
