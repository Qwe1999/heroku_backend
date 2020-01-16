package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.*;
import com.softserve.academy.event.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.*;

@Mapper(componentModel = "spring", imports = {StatisticResolver.class})
@Service
public interface GeneralStatisticMapper {

    @Mapping(target = "questionDTOS", source = "surveyQuestions")
    QuestionsGeneralStatisticDTO toQuestionsDTO(Survey survey);

    @Mapping(target = "choiceAnswers", expression = "java(StatisticResolver.parseChoiceAnswers(surveyQuestion))")
    @Mapping(target = "answers", expression = "java(StatisticResolver.parseAllAnswers(surveyQuestion.getSurveyAnswers()))")
    OneQuestionGeneralStatisticDTO toOneQuestionDTO(SurveyQuestion surveyQuestion);

    List<OneQuestionGeneralStatisticDTO> listOneQuestionToDTO(List<SurveyQuestion> surveyQuestions);


}
