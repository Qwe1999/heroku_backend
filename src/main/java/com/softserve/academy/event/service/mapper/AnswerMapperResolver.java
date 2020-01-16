package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.db.QuestionService;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AnswerMapperResolver {

    private ContactService contactService;
    private QuestionService questionService;

    @Autowired
    public AnswerMapperResolver(ContactService contactService, QuestionService questionService) {
        this.contactService = contactService;
        this.questionService = questionService;
    }

    @Named("getQuestion")
    public SurveyQuestion getQuestion(Long questionId){
        Optional<SurveyQuestion> optionalQuestion = questionService.findFirstById(questionId);
        return optionalQuestion.orElse(null);
    }

    @Named("getContact")
    public Contact getContact(Long contactId){
        Optional<Contact> optionalContact = contactService.findFirstById(contactId);
        return optionalContact.orElse(null);
    }

    @Named("getAnswers")
    public String getAnswers(Object answers) throws JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        if(answers instanceof List){
            return objectMapper.writeValueAsString(answers);
        } else
            return stringBuilder.append("[")
                    .append(objectMapper.writeValueAsString(answers))
                    .append("]")
                    .toString();
    }
}
