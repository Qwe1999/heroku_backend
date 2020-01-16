package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.exception.EmailExistException;
import com.softserve.academy.event.exception.SurveyNotFound;
import com.softserve.academy.event.exception.UserNotFound;
import com.softserve.academy.event.repository.ContactRepository;
import com.softserve.academy.event.repository.SurveyContactConnectorRepository;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.repository.UserRepository;
import com.softserve.academy.event.service.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Optional;

@PropertySource("classpath:application.properties")
@Service
@Transactional
public class EmailServiceImpl implements EmailService {
    @Value("${app.frontend.url}")
    private String baseUrl;
    private static final String END_POINT = "test/";
    private final JavaMailSender javaMailSender;
    private final SurveyRepository surveyRepository;
    private final ContactRepository contactRepository;
    private final SurveyContactConnectorRepository surveyContactRepository;
    private final UserRepository userRepository;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, SurveyRepository surveyRepository, ContactRepository contactRepository, SurveyContactConnectorRepository surveyContactRepository, UserRepository userRepository) {
        this.javaMailSender = javaMailSender;
        this.surveyRepository = surveyRepository;
        this.contactRepository = contactRepository;
        this.surveyContactRepository = surveyContactRepository;
        this.userRepository = userRepository;
    }

    public void sendEmailForUser(String idUser, String idSurvey, String[] emails) {
        User user = userRepository.findFirstById(Long.valueOf(idUser)).orElseThrow(UserNotFound::new);
        for (String anEmail : emails) {
            Optional<Survey> survey = surveyRepository.findFirstById(Long.parseLong(idSurvey));
            newSurveyContact(survey.orElseThrow(SurveyNotFound::new), newContact(user, anEmail));
        }
        String subject = "Survey";
        for (String anEmail : emails) {
            String codEmail = anEmail + ";" + idSurvey;
            String encodedString = baseUrl + END_POINT + Base64.getEncoder().withoutPadding().encodeToString(codEmail.getBytes());
            String message = "Message from " + user.getEmail() + ": " + "<<Please, follow the link and take the survey" + " " + encodedString + " >>";
            sendMail(anEmail, subject, message);
        }
    }

    private void newSurveyContact(Survey survey, Contact contact) {
        SurveyContact surveyContact = new SurveyContact();
        surveyContact.setContact(contact);
        surveyContact.setSurvey(survey);
        surveyContact.setCanPass(true);
        surveyContactRepository.save(surveyContact);
    }

    private boolean emailExists(String email) {
        return contactRepository.findByEmail(email).isPresent();
    }

    private Contact newContact(User user, String anEmail) {
        if (emailExists(anEmail)) {
            throw new EmailExistException("There is an contact with that email address: " + anEmail);
        }
        Contact contact = new Contact();
        contact.setEmail(anEmail);
        contact.setUser(user);
        return contactRepository.save(contact);
    }

    @Override
    public void sendMail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        javaMailSender.send(email);
    }
}
