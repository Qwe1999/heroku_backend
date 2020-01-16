package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.repository.QuestionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionRepositoryImpl extends BasicRepositoryImpl<SurveyQuestion, Long> implements QuestionRepository{

    @Override
    public List<SurveyQuestion> findBySurveyId(Long surveyId) {
        return sessionFactory.getCurrentSession()
                .createQuery("from " + SurveyQuestion.class.getName() + " where survey_id = :surveyId ORDER BY index")
                .setParameter("surveyId", surveyId)
                .getResultList();
    }
}
