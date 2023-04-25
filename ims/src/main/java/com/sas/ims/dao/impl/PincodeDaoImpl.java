package com.sas.ims.dao.impl;

import com.sas.ims.dao.PincodeDao;
import com.sas.ims.entity.PincodeMaster;
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
public class PincodeDaoImpl implements PincodeDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PincodeMaster> findAllPincodeMaster(FilterRequest filterRequest) throws BadRequestException {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<PincodeMaster> criteria = criteriaBuilder.createQuery(PincodeMaster.class);
            Root<PincodeMaster> pincodeMasterRoot = criteria.from(PincodeMaster.class);
            pincodeMasterRoot.fetch("stateMaster");
            pincodeMasterRoot.fetch("districtMaster");
            List<Predicate> predicates = getPredicates(filterRequest, criteriaBuilder, pincodeMasterRoot);
            criteria.select(pincodeMasterRoot).where(predicates.toArray(new Predicate[0]));
            return entityManager.createQuery(criteria).setFirstResult(filterRequest.getStart()).setMaxResults(filterRequest.getLimit()).getResultList();
        } catch (Exception exception) {
            throw new BadRequestException(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private List<Predicate> getPredicates(FilterRequest filterRequest, CriteriaBuilder criteriaBuilder, Root<PincodeMaster> pincodeMasterRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (filterRequest.getStateId() != null) {
            predicates.add(criteriaBuilder.equal(pincodeMasterRoot.get("stateId"), filterRequest.getStateId()));
        }
        if (filterRequest.getDistrictId() != null) {
            predicates.add(criteriaBuilder.equal(pincodeMasterRoot.get("districtId"), filterRequest.getDistrictId()));
        }
        if (filterRequest.getPincode() != null) {
            predicates.add(criteriaBuilder.equal(pincodeMasterRoot.get("pincodeMasterPK").get("pincode"), filterRequest.getPincode()));
        }
        return predicates;
    }

    @Override
    public Long findAllPincodeMasterCount(FilterRequest filterRequest) throws BadRequestException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
        Root<PincodeMaster> pincodeMasterRoot = criteria.from(PincodeMaster.class);
        try {
            List<Predicate> predicates = getPredicates(filterRequest, criteriaBuilder, pincodeMasterRoot);
            criteria.select(criteriaBuilder.count(pincodeMasterRoot)).where(predicates.toArray(new Predicate[0]));
            return entityManager.createQuery(criteria).getSingleResult();
        } catch (Exception exception) {
            throw new BadRequestException(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}