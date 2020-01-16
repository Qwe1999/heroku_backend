package com.softserve.academy.event.service.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.EditSurveyDTO;
import com.softserve.academy.event.dto.EditSurveyQuestionDTO;
import com.softserve.academy.event.dto.SaveSurveyDTO;
import com.softserve.academy.event.dto.SurveyQuestionDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring", imports = {Arrays.class, ObjectMapper.class, String.class})
@Service
public interface SaveQuestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "choiceAnswers",
            expression = "java(new ObjectMapper().writeValueAsString(surveyQuestionDTO.getChoiceAnswers()))")
    SurveyQuestion toEntity(SurveyQuestionDTO surveyQuestionDTO) throws JsonProcessingException;


    @Mapping(target = "choiceAnswers", expression = "java(Arrays.asList(new ObjectMapper().readValue(surveyQuestions.getChoiceAnswers(),String[].class)))")
    EditSurveyQuestionDTO toEditSurveyQuestionDTO(SurveyQuestion surveyQuestions) throws JsonProcessingException;

    @Mapping(target = "imageUrl", source = "surveyPhotoName")
    Survey toSurvey(SaveSurveyDTO saveSurveyDTO);

    @Mapping(target = "surveyPhotoName", expression = "java(survey.getImageUrl())")
    @Mapping(target = "title", expression = "java(survey.getTitle())")
    EditSurveyDTO toEditSurveyDTO(Survey survey, List<EditSurveyQuestionDTO> questions);
}

