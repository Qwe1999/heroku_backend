package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;
import com.softserve.academy.event.repository.SurveyContactConnectorRepository;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SurveyContactConnectorServiceImpl implements SurveyContactConnectorService {

    private final SurveyContactConnectorRepository repository;

    @Autowired
    public SurveyContactConnectorServiceImpl(SurveyContactConnectorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<SurveyContact> findFirstById(Long id) {
        return repository.findFirstById(id);
    }

    @Override
    public List<SurveyContact> findAll() {
        return repository.findAll();
    }

    @Override
    public SurveyContact save(SurveyContact entity) {
        return repository.save(entity);
    }

    @Override
    public SurveyContact update(SurveyContact object) {
        return repository.update(object);
    }

    @Override
    public void delete(SurveyContact entity) {
        repository.delete(entity);
    }

    @Override
    public boolean isEnable(Long contactId, Long surveyId) throws IncorrectLinkException, SurveyAlreadyPassedException {
        return repository.isEnable(contactId, surveyId);
    }

    @Override
    public Optional<SurveyContact> findByContactAndSurvey(Long contactId, Long surveyId) {
        return repository.findByContactAndSurvey(contactId, surveyId);
    }
}
