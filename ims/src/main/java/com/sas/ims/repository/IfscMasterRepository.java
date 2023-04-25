package com.sas.ims.repository;

import com.sas.ims.entity.IfscMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IfscMasterRepository extends JpaRepository<IfscMaster, String> {
    Optional<IfscMaster> findByIfscCodeContainingIgnoreCase(String ifscCode);

    Optional<IfscMaster> findByIfscCodeAndStatus(String ifscCode, String status);

    @Query(value = "select * from ifsc_master where (ifsc_code like :ifscCode or upper(ifsc_code) like :ifscCode) ", nativeQuery = true)
    List<IfscMaster> findAllByIfscMaster(String ifscCode);
}