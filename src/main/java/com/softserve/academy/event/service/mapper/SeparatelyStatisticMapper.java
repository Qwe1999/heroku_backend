package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.OneQuestionSeparatelyStatisticDTO;
import com.softserve.academy.event.dto.QuestionsSeparatelyStatisticDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.entity.SurveyQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.*;

@Mapper(componentModel = "spring", imports = {StatisticResolver.class})
@Service
public interface SeparatelyStatisticMapper {

    default Set<QuestionsSeparatelyStatisticDTO> toSetQuestionsDTO(Survey survey)  {
        if (survey == null) {
            return new HashSet<>();
        }
        Set<QuestionsSeparatelyStatisticDTO> hashSet = new HashSet<>();
        survey.getSurveyContacts().forEach(surveyContact ->
            hashSet.add(toQuestionsDTO(surveyContact, survey))
        );
        return hashSet;
    }

    @Mapping(target = "questionDTOS",
            expression = "java(toSetOneQuestionDTO(survey.getSurveyQuestions(),contact))")
    @Mapping(target = "email", expression = "java(contact.getContact().getEmail())")
    QuestionsSeparatelyStatisticDTO toQuestionsDTO(SurveyContact contact, Survey survey);

    default Set<OneQuestionSeparatelyStatisticDTO> toSetOneQuestionDTO(
            List<SurveyQuestion> surveyQuestions, SurveyContact contact){
        if (surveyQuestions == null || contact == null) {
            return new HashSet<>();
        }
        Set<OneQuestionSeparatelyStatisticDTO> hashSet = new HashSet<>();
        surveyQuestions.forEach(surveyQuestion ->
            hashSet.add(toOneQuestionDTO(surveyQuestion, contact))
        );
        return hashSet;
    }

    @Mapping(target = "choiceAnswers",
            expression = "java(StatisticResolver.parseChoiceAnswers(surveyQuestion))")
    @Mapping(target = "answer",
            expression = "java(StatisticResolver.parseAnswer(surveyQuestion.getSurveyAnswers(),contact))")
    OneQuestionSeparatelyStatisticDTO toOneQuestionDTO(SurveyQuestion surveyQuestion, SurveyContact contact);


}
