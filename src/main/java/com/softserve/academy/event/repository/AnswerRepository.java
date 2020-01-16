package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.SurveyAnswer;

import java.util.List;

public interface AnswerRepository extends BasicRepository<SurveyAnswer, Long> {

    List<SurveyAnswer> findByQuestionId(Long questionId);
}
