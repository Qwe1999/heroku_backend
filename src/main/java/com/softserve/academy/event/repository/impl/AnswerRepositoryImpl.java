package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.repository.AnswerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnswerRepositoryImpl extends BasicRepositoryImpl<SurveyAnswer, Long> implements AnswerRepository {


    @Override
    public List<SurveyAnswer> findByQuestionId(Long questionId) {
        return sessionFactory.getCurrentSession()
                .createNamedQuery("SurveyAnswer.findByQuestionId")
                .setParameter("questionId",questionId).getResultList();
    }
}