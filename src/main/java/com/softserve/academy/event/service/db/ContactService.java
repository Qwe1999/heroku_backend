package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.Contact;

import java.util.Optional;

public interface ContactService extends BasicService<Contact, Long> {

    Optional<Long> getIdByEmail(String email);

    boolean canPass(Long surveyId, String contactEmail);
}
