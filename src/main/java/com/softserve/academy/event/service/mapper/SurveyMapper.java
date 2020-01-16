package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
@Service
public interface SurveyMapper {

    SurveyDTO toDTO(Survey survey, Long countAnswers, Long countContacts);

}
