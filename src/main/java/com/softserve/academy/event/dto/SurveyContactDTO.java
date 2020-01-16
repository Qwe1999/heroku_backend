package com.softserve.academy.event.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SurveyContactDTO {

    private Long surveyId;
    private String contactEmail;
    private List<QuestionDTO> questions;
}