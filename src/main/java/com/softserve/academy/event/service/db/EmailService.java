package com.softserve.academy.event.service.db;

public interface EmailService {

    void sendMail(String recipientAddress, String subject, String message);


}