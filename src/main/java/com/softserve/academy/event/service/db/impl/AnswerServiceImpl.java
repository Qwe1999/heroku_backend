package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.repository.AnswerRepository;
import com.softserve.academy.event.service.db.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public Optional<SurveyAnswer> findFirstById(Long id) {
        return answerRepository.findFirstById(id);
    }

    @Override
    public List<SurveyAnswer> findAll() {
        return answerRepository.findAll();
    }

    @Override
    public SurveyAnswer save(SurveyAnswer entity) {
        return answerRepository.save(entity);
    }

    @Override
    public SurveyAnswer update(SurveyAnswer object) {
        return answerRepository.update(object);
    }

    @Override
    public void delete(SurveyAnswer entity) {
        answerRepository.delete(entity);
    }
    
}
