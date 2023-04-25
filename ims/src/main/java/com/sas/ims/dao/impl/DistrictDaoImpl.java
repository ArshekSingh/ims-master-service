package com.sas.ims.dao.impl;

import com.sas.ims.dao.DistrictDao;
import com.sas.ims.entity.DistrictMaster;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.request.FilterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DistrictDaoImpl implements DistrictDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DistrictMaster> findAllDistrictMaster(FilterRequest filterRequest) throws BadRequestException {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<DistrictMaster> criteria = criteriaBuilder.createQuery(DistrictMaster.class);
            Root<DistrictMaster> districtMasterRoot = criteria.from(DistrictMaster.class);
            districtMasterRoot.fetch("stateMaster");
            List<Predicate> predicates = getPredicates(filterRequest, criteriaBuilder, districtMasterRoot);
            criteria.select(districtMasterRoot).where(predicates.toArray(new Predicate[0]));
            return entityManager.createQuery(criteria).setFirstResult(filterRequest.getStart()).setMaxResults(filterRequest.getLimit()).getResultList();
        } catch (Exception exception) {
            throw new BadRequestException(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private List<Predicate> getPredicates(FilterRequest filterRequest, CriteriaBuilder criteriaBuilder, Root<DistrictMaster> districtMasterRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (filterRequest.getStateId() != null) {
            predicates.add(criteriaBuilder.equal(districtMasterRoot.get("stateId"), filterRequest.getStateId()));
        }
        return predicates;
    }

    @Override
    public Long findAllDistrictMasterCount(FilterRequest filterRequest) throws BadRequestException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
        Root<DistrictMaster> districtMasterRoot = criteria.from(DistrictMaster.class);
        try {
            List<Predicate> predicates = getPredicates(filterRequest, criteriaBuilder, districtMasterRoot);
            criteria.select(criteriaBuilder.count(districtMasterRoot)).where(predicates.toArray(new Predicate[0]));
            return entityManager.createQuery(criteria).getSingleResult();
        } catch (Exception exception) {
            throw new BadRequestException(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
