package com.sas.ims.dao.impl;

import com.sas.ims.dao.RequestOrderDao;
import com.sas.ims.entity.RequestOrder;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.utils.DateTimeUtil;
import com.sas.tokenlib.utils.UserSession;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RequestOrderDaoImpl implements RequestOrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    private List<Predicate> getPredicates(FilterRequest filterRequest, CriteriaBuilder criteriaBuilder, Root<RequestOrder> requestOrderRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.hasText(filterRequest.getBatchId())) {
            predicates.add(criteriaBuilder.equal(requestOrderRoot.get("batchId"), filterRequest.getBatchId()));
        }
        if (StringUtils.hasText(filterRequest.getRequestOrderStartDate()) && StringUtils.hasText(filterRequest.getRequestOrderEndDate())) {
            LocalDateTime startDate = DateTimeUtil.stringToLocalDateTime(filterRequest.getRequestOrderStartDate(), DateTimeUtil.DDMMYYYY);
            LocalDateTime endDate = DateTimeUtil.stringToLocalDateTime(filterRequest.getRequestOrderEndDate(), DateTimeUtil.DDMMYYYY);

            predicates.add(criteriaBuilder.between(requestOrderRoot.get("requestOrderDate"), startDate, endDate));
        }
        if (StringUtils.hasText(filterRequest.getRequestOrderId())) {
            predicates.add(criteriaBuilder.equal(requestOrderRoot.get("requestOrderId"), filterRequest.getRequestOrderId()));
        }
        if (StringUtils.hasText(filterRequest.getStatus())) {
            predicates.add(criteriaBuilder.equal(requestOrderRoot.get("status"), filterRequest.getStatus()));
        } else {
            predicates.add(criteriaBuilder.equal(requestOrderRoot.get("status"), "P"));
        }
        return predicates;
    }

    @Override
    public List<RequestOrder> findAllRequestOrder(FilterRequest filterRequest, UserSession userSession) throws BadRequestException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RequestOrder> query = cb.createQuery(RequestOrder.class);
        Root<RequestOrder> root = query.from(RequestOrder.class);
        List<Predicate> predicates = getPredicates(filterRequest, cb, root);
        query.select(root).where(predicates.toArray(new Predicate[0]));
        TypedQuery<RequestOrder> typedQuery = entityManager.createQuery(query);
        if (filterRequest.getPage() == 0) {
            typedQuery.setFirstResult(0);
        } else {
            typedQuery.setFirstResult((filterRequest.getPage()) * filterRequest.getSize());
        }
        typedQuery.setMaxResults(filterRequest.getSize());
        return typedQuery.getResultList();
    }

    @Override
    public Long findAllRequestOrderCount(FilterRequest filterRequest, UserSession userSession) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<RequestOrder> root = countQuery.from(RequestOrder.class);
        List<Predicate> predicates = getPredicates(filterRequest, cb, root);
        countQuery.select(cb.count(root)).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

}
