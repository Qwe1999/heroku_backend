package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.exception.IncorrectDataDB;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class StatisticResolver {

    @Named("parseChoiceAnswers")
    static public List<String> parseChoiceAnswers(SurveyQuestion surveyQuestion){
        try {
            return Arrays.asList(new ObjectMapper().readValue(
                    surveyQuestion.getChoiceAnswers(),String[].class));
        } catch (JsonProcessingException e) {
            throw new IncorrectDataDB(String.format("ChoiceAnswers = %s incorrect in SurveyQuestion with id = %s",
                    surveyQuestion.getChoiceAnswers(),surveyQuestion.getId()));
        }
    }

    @Named("parseAllAnswers")
    static public List<List<String>> parseAllAnswers(Set<SurveyAnswer> surveyAnswerSet) {
        return surveyAnswerSet.stream()
                .map(surveyAnswer -> {
                    try {
                        return Arrays.asList(new ObjectMapper().readValue(surveyAnswer.getValue(), String[].class));
                    } catch (JsonProcessingException e) {
                        throw new IncorrectDataDB(String.format("Value = %s incorrect in surveyAnswer with id = %s",
                                surveyAnswer.getValue(),surveyAnswer.getId()));
                    }
                }).collect(Collectors.toList());
    }

    @Named("parseAnswer")
    static public List<String> parseAnswer
            (Set<SurveyAnswer> surveyAnswerSet, SurveyContact contact) {
        return surveyAnswerSet.stream()
                .filter(surveyAnswer ->surveyAnswer.getContact().equals(contact.getContact()))
                .map(answer -> {
                    try {
                        return new ObjectMapper().readValue(answer.getValue(), String[].class);
                    } catch (JsonProcessingException e) {
                        throw new IncorrectDataDB(String.format("Value = %s incorrect in surveyAnswer with id = %s",
                                answer.getValue(),answer.getId()));
                    }
                })
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }


}
