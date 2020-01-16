package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.EmailDTO;
import com.softserve.academy.event.exception.IncorrectEmailsException;
import com.softserve.academy.event.exception.UserNotFound;
import com.softserve.academy.event.service.db.EmailService;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.util.EmailValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@Slf4j
public class SendEmailController {
    private final EmailService emailService;
    private final UserService service;

    @Autowired
    public SendEmailController(EmailService emailService, UserService service) {
        this.service = service;
        this.emailService = emailService;
    }

    @PostMapping("/sendEmails")
    public void doSendEmails(@RequestBody EmailDTO emailDTO) {
        String[] emails = emailDTO.getEmailsArray();
        EmailValidator.validate(emails);
        String idUser = service.getAuthenticationId().orElseThrow(UserNotFound::new).toString();
        String idSurvey = emailDTO.getSurveyId();
        emailService.sendEmailForUser(idUser, idSurvey, emails);
    }

    @ExceptionHandler(IncorrectEmailsException.class)
    public ResponseEntity<String> incorrectEmailsHandler(Exception e, WebRequest request) {
        log.error("Incorrect emails : ", e);
        log.error(request.getDescription(false));
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
