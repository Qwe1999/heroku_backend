package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Contact;

import java.util.Optional;

public interface ContactRepository extends BasicRepository<Contact, Long> {

    Optional<Contact> findByEmail(String email);

    Optional<Long> getIdByEmail(String email);

    Contact getEmailAndUserId(String email, Long userId);
}