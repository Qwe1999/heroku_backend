package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.repository.AnswerRepository;
import com.softserve.academy.event.repository.QuestionRepository;
import com.softserve.academy.event.service.db.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Optional<SurveyQuestion> findFirstById(Long id) {
        return questionRepository.findFirstById(id);
    }

    @Override
    public List<SurveyQuestion> findAll() {
        return questionRepository.findAll();
    }

    @Override
    public SurveyQuestion save(SurveyQuestion entity) {
        return questionRepository.save(entity);
    }

    @Override
    public SurveyQuestion update(SurveyQuestion object) {
        return questionRepository.update(object);
    }

    @Override
    public void delete(SurveyQuestion entity) {
        questionRepository.delete(entity);
    }

    @Override
    public List<SurveyQuestion> findBySurveyId(Long surveyId) {
        return questionRepository.findBySurveyId(surveyId);
    }

}
