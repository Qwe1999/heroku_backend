package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.SurveyQuestion;

import java.util.List;

public interface QuestionRepository extends BasicRepository<SurveyQuestion, Long>{

    List<SurveyQuestion> findBySurveyId(Long surveyId);
}
