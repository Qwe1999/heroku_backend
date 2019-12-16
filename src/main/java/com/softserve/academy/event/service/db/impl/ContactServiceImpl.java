package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.repository.ContactRepository;
import com.softserve.academy.event.service.db.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactServiceImpl extends BasicServiceImpl<Contact, Long> implements ContactService {
    private final ContactRepository repository;

    @Autowired
    public ContactServiceImpl(ContactRepository repository){
        this.repository = repository;
    }


    @Override
    public Optional<Long> getIdByEmail(String email) {
        return repository.getIdByEmail(email);
    }
}
