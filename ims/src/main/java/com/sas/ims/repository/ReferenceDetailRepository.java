package com.sas.ims.repository;

import com.sas.ims.entity.ReferenceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReferenceDetailRepository extends JpaRepository<ReferenceDetail, String> {

    Optional<List<ReferenceDetail>> findByReferenceDetailPK_ReferenceDomainOrderByValue4(String referenceDomain);

    List<ReferenceDetail> findByReferenceDetailPK_ReferenceDomainInOrderByValue4(List<String> referenceDomain);

    @Query(value = "select DISTINCT  REFERENCE_DOMAIN from reference_detail where REFERENCE_DOMAIN like :referenceDomain%", nativeQuery = true)
    List<String> findByReferenceDomain(String referenceDomain);

    ReferenceDetail findByReferenceDetailPK_ReferenceDomainAndReferenceDetailPK_KeyValue(String referenceDomain, String key);

    List<ReferenceDetail> findByReferenceDetailPK_ReferenceDomain(String rd_relationship);

    List<ReferenceDetail> findByReferenceDetailPK_ReferenceDomainIn(List<String> rd_relationship);


}