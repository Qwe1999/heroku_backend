package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.AnswerDTO;

import com.softserve.academy.event.entity.SurveyAnswer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring", uses = AnswerMapperResolver.class)
@Service
public interface AnswerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", source = "questionId", qualifiedByName = "getQuestion")
    @Mapping(target = "contact", source = "contactId", qualifiedByName = "getContact")
    @Mapping(target = "value", source = "answers", qualifiedByName = "getAnswers")
    SurveyAnswer toEntity(AnswerDTO answerDTO);

}
