package com.sas.ims.repository;

import com.sas.ims.entity.RequestOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestOrderRepository extends JpaRepository<RequestOrder, Long> {
    Optional<RequestOrder> findByRequestOrderIdAndOrgId(Long requestOrderId, Long orgId);

    Optional<List<RequestOrder>> findAllByOrgIdAndRequestOrderIdIn(Long orgId, List<Long> requestOrderIds);

    Optional<RequestOrder> findByLoanIdAndOrgId(Long requestOrderId, Long orgId);

    Optional<RequestOrder> findByRequestOrderIdAndOrgIdAndStatus(Long requestOrderId, Long orgId, String status);

    @Transactional
    @Modifying
    @Query(value = "update request_order set status='C' where org_id =:companyId and request_order_id =:requestOrderId", nativeQuery = true)
    void updateRequestOrderStatusToComplete(Long companyId, Long requestOrderId);
}
