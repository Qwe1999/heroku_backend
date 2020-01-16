package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;
import com.softserve.academy.event.repository.ContactRepository;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;
    private final SurveyContactConnectorService surveyContactConnectorService;

    @Autowired
    public ContactServiceImpl(ContactRepository repository, SurveyContactConnectorService surveyContactConnectorService) {
        this.repository = repository;
        this.surveyContactConnectorService = surveyContactConnectorService;
    }

    @Override
    public Optional<Long> getIdByEmail(String email) {
        return repository.getIdByEmail(email);
    }

    @Override
    public boolean canPass(Long surveyId, String contactEmail) {
        Optional<Long> contactId = getIdByEmail(contactEmail);
        try {
            return surveyContactConnectorService.isEnable(contactId.orElse(null), surveyId);
        } catch (IncorrectLinkException | SurveyAlreadyPassedException e) {
            return false;
        }
    }

    @Override
    public Optional<Contact> findFirstById(Long id) {
        return repository.findFirstById(id);
    }

    @Override
    public List<Contact> findAll() {
        return repository.findAll();
    }

    @Override
    public Contact save(Contact entity) {
        return repository.save(entity);
    }

    @Override
    public Contact update(Contact object) {
        return repository.update(object);
    }

    @Override
    public void delete(Contact entity) {
        repository.delete(entity);
    }

}