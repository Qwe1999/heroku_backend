package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.repository.SurveyContactConnectorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class SurveyContactConnectorRepositoryImpl extends BasicRepositoryImpl<SurveyContact, Long> implements SurveyContactConnectorRepository {

    private final String fromQuery = "from " + clazz.getName() + " as t where t.contact.id = :contactId and t.survey.id = :surveyId";
    private static final String CONTACT_ID = "contactId";
    private static final String SURVEY_ID = "surveyId";

    @Override
    public boolean isEnable(Long contactId, Long surveyId) throws IncorrectLinkException {
        List res = sessionFactory.getCurrentSession()
                .createQuery("select t.canPass " + fromQuery)
                .setParameter(CONTACT_ID, contactId)
                .setParameter(SURVEY_ID, surveyId)
                .setMaxResults(1)
                .getResultList();
        if (res.isEmpty()) {
            log.error("This survey is not available for the current user");
            throw new IncorrectLinkException("Sorry, but you can`t pass survey by this link");
        }
        return (boolean) res.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<SurveyContact> findByContactAndSurvey(Long contactId, Long surveyId) {
        List<SurveyContact> result = sessionFactory.getCurrentSession()
                .createQuery(fromQuery)
                .setParameter(SURVEY_ID, surveyId)
                .setParameter(CONTACT_ID, contactId)
                .getResultList();
        if (result.isEmpty())
            return Optional.empty();
        return Optional.of(result.get(0));
    }
}