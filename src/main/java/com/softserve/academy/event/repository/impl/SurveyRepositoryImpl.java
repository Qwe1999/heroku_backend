package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.exception.SurveyNotFound;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.ParameterMode;
import java.math.BigInteger;
import java.util.Optional;

@Repository
public class SurveyRepositoryImpl extends BasicRepositoryImpl<Survey, Long> implements SurveyRepository {

    @Override
    public Page<Survey> findAllByPageableAndUserEmail(Pageable pageable, String userEmail) {
        Session session = sessionFactory.getCurrentSession();
        return getSurveyPage(pageable, session, userEmail,
                SurveyStatus.TEMPLATE, "s.status != :status");
    }

    @Override
    public Page<Survey> findAllByPageableAndStatusAndUserEmail(Pageable pageable,
                                                               SurveyStatus status, String userEmail) {
        Session session = sessionFactory.getCurrentSession();
        return getSurveyPage(pageable, session, userEmail, status, "s.status = :status");
    }

    @SuppressWarnings("unchecked")
    private Page<Survey> getSurveyPage(Pageable pageable, Session session,
                                       String userEmail, SurveyStatus status, String statusQuery) {
        Query query = session.createQuery("from " + clazz.getName() + " as s" +
                " left join fetch s.surveyContacts c" +
                " where s.user.email = :userEmail and s.active = true and " + statusQuery +
                " group by s.id, c.id order by s." + pageable.sorting())
                .setParameter("userEmail", userEmail)
                .setParameter("status", status);
        Long countResult = (Long) session.createQuery("select count(s) from " + clazz.getName() +
                " as s where s.user.email = :userEmail and s.active = true and " + statusQuery)
                .setParameter("userEmail", userEmail)
                .setParameter("status", status).uniqueResult();
        query.setFirstResult(pageable.getCurrentPage() * pageable.getSize());
        query.setMaxResults(pageable.getSize());
        pageable.setLastPage((int) Math.ceil((double) countResult / pageable.getSize()));
        pageable.setCurrentPage(pageable.getCurrentPage() + 1);
        return new Page<>(query.list(), pageable);
    }

    @Override
    public Optional<BigInteger> cloneSurvey(DuplicateSurveySettings settings) {
        return Optional.of((BigInteger) sessionFactory.getCurrentSession()
                .createStoredProcedureQuery("clone_survey")
                .registerStoredProcedureParameter("id",Long.class,ParameterMode.IN)
                .registerStoredProcedureParameter("clearContacts",Boolean.class,ParameterMode.IN)
                .setParameter("id", settings.getId())
                .setParameter("clearContacts", settings.isClearContacts())
                .getSingleResult());
    }

}
