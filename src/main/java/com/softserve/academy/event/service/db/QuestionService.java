package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.SurveyQuestion;

import java.util.List;

public interface QuestionService extends BasicService<SurveyQuestion, Long>{

    List<SurveyQuestion> findBySurveyId(Long surveyId);

}
