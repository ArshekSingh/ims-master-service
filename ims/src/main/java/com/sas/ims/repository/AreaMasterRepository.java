package com.sas.ims.repository;

import com.sas.ims.entity.AreaMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaMasterRepository extends JpaRepository<AreaMaster, Long> {
    Optional<AreaMaster> findByAreaIdAndOrgId(Long areaId, Long orgId);

    Optional<List<AreaMaster>> findAllByAreaIdAndOrgId(Long areaId, Long orgId);

    Optional<AreaMaster> findByAreaTypeAndOrgIdAndStatus(String areaType, Long orgId, String status);

    Page<AreaMaster> findAllByAreaTypeAndOrgIdAndStatus(String areaType, Long orgId, String status, Pageable pageable);

    Page<AreaMaster> findAll(Specification<AreaMaster> spec, Pageable pageable);

    boolean existsByOrgIdAndAreaName(Long orgId, String areaName);

    List<AreaMaster> findAllByOrgIdAndAreaType(Long companyId, String areaType);

    @Query(value = "select nextval('area_master_area_id_seq')", nativeQuery = true)
    Long findLatestSequence();
}
