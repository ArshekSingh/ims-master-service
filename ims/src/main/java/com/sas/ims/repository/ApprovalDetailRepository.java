package com.sas.ims.repository;

import com.sas.ims.entity.ApprovalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalDetailRepository extends JpaRepository<ApprovalDetail, Long> {

}
