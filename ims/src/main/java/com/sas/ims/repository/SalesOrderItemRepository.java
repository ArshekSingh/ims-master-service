package com.sas.ims.repository;

import com.sas.ims.entity.AreaMaster;
import com.sas.ims.entity.SalesOrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalesOrderItemRepository extends JpaRepository<SalesOrderItem, Long> {

    @Query(value = "select nextval('sales_order_item_id_seq')", nativeQuery = true)
    Long findLatestSequence();

    Optional<List<SalesOrderItem>> findByOrgIdAndSalesOrderIdIn(Long orgId, List<Long> salesOrderIds);

    Page<SalesOrderItem> findAllByOrgIdAndSalesOrderId(Long orgId, Long salesOrderId, Pageable pageable);

    @Query(value = "select count(*) from sales_order_item where org_id =:orgId and sales_order_id =:salesOrderId", nativeQuery = true)
    Long findTotalSalesItemCount(Long orgId, Long salesOrderId);

}
