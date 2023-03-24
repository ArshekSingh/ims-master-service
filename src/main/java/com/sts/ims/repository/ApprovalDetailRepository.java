package com.sts.ims.repository;

import com.sts.ims.entity.ApprovalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalDetailRepository extends JpaRepository<ApprovalDetail, Long> {

}
