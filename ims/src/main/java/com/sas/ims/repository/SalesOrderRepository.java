package com.sas.ims.repository;

import com.sas.ims.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    @Query(value = "select nextval('sales_order_id_seq')", nativeQuery = true)
    Long findLatestSequence();

    Optional<SalesOrder> findByOrgIdAndId(Long orgId, Long id);
}
