package com.sas.ims.dao.impl;

import com.sas.ims.entity.IfscMaster;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.repository.IfscDao;
import com.sas.ims.request.FilterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IfscDaoImpl implements IfscDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<IfscMaster> findAllIfscCodeMaster(FilterRequest filterRequest) throws BadRequestException {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<IfscMaster> criteria = criteriaBuilder.createQuery(IfscMaster.class);
            Root<IfscMaster> ifscMasterRoot = criteria.from(IfscMaster.class);
            ifscMasterRoot.fetch("stateMaster");
            List<Predicate> predicates = getPredicates(filterRequest, criteriaBuilder, ifscMasterRoot);
            criteria.select(ifscMasterRoot).where(predicates.toArray(new Predicate[0]));
            return entityManager.createQuery(criteria).setFirstResult(filterRequest.getStart()).setMaxResults(filterRequest.getLimit()).getResultList();
        } catch (Exception exception) {
            throw new BadRequestException(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private List<Predicate> getPredicates(FilterRequest filterRequest, CriteriaBuilder criteriaBuilder, Root<IfscMaster> ifscMasterRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.hasText(filterRequest.getIfscCode())) {
            predicates.add(criteriaBuilder.equal(ifscMasterRoot.get("ifscCode"), filterRequest.getIfscCode()));
        }
        if (StringUtils.hasText(filterRequest.getBankName())) {
            predicates.add(criteriaBuilder.equal(ifscMasterRoot.get("bankName"), filterRequest.getBankName()));
        }
        return predicates;
    }

    @Override
    public Long findAllIfscCodeCount(FilterRequest filterRequest) throws BadRequestException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
        Root<IfscMaster> ifscMasterRoot = criteria.from(IfscMaster.class);
        try {
            List<Predicate> predicates = getPredicates(filterRequest, criteriaBuilder, ifscMasterRoot);
            criteria.select(criteriaBuilder.count(ifscMasterRoot)).where(predicates.toArray(new Predicate[0]));
            return entityManager.createQuery(criteria).getSingleResult();
        } catch (Exception exception) {
            throw new BadRequestException(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}