package com.sas.ims.dao.impl;

import com.sas.ims.dao.SalesOrderDao;
import com.sas.ims.entity.SalesOrder;
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
public class SalesOrderDaoImpl implements SalesOrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    private List<Predicate> getPredicates(FilterRequest filterRequest, CriteriaBuilder criteriaBuilder, Root<SalesOrder> requestOrderRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.hasText(filterRequest.getBatchId())) {
            predicates.add(criteriaBuilder.equal(requestOrderRoot.get("batchId"), filterRequest.getBatchId()));
        }
        if (StringUtils.hasText(filterRequest.getSalesOrderStartDate()) && StringUtils.hasText(filterRequest.getSalesOrderEndDate())) {
            LocalDateTime startDate = DateTimeUtil.stringToLocalDateTime(filterRequest.getSalesOrderStartDate(), DateTimeUtil.DDMMYYYY);
            LocalDateTime endDate = DateTimeUtil.stringToLocalDateTime(filterRequest.getSalesOrderEndDate(), DateTimeUtil.DDMMYYYY);

            predicates.add(criteriaBuilder.between(requestOrderRoot.get("insertedOn"), startDate, endDate));
        }
        if (StringUtils.hasText(filterRequest.getSalesOrderId())) {
            predicates.add(criteriaBuilder.equal(requestOrderRoot.get("salesOrderId"), filterRequest.getSalesOrderId()));
        }
        if (StringUtils.hasText(filterRequest.getStatus())) {
            predicates.add(criteriaBuilder.equal(requestOrderRoot.get("status"), filterRequest.getStatus()));
        } else {
            predicates.add(criteriaBuilder.equal(requestOrderRoot.get("status"), "P"));
        }
        return predicates;
    }

    @Override
    public List<SalesOrder> findAllSalesOrder(FilterRequest filterRequest, UserSession userSession) throws BadRequestException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SalesOrder> query = cb.createQuery(SalesOrder.class);
        Root<SalesOrder> root = query.from(SalesOrder.class);
        List<Predicate> predicates = getPredicates(filterRequest, cb, root);
        query.select(root).where(predicates.toArray(new Predicate[0]));
        TypedQuery<SalesOrder> typedQuery = entityManager.createQuery(query);
        if (filterRequest.getPage() == 0) {
            typedQuery.setFirstResult(0);
        } else {
            typedQuery.setFirstResult((filterRequest.getPage()) * filterRequest.getSize());
        }
        typedQuery.setMaxResults(filterRequest.getSize());
        return typedQuery.getResultList();
    }

    @Override
    public Long findAllSalesOrderCount(FilterRequest filterRequest, UserSession userSession) throws BadRequestException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<SalesOrder> root = countQuery.from(SalesOrder.class);
        List<Predicate> predicates = getPredicates(filterRequest, cb, root);
        countQuery.select(cb.count(root)).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

}
