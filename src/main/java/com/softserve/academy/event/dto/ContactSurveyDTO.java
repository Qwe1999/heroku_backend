package com.softserve.academy.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ContactSurveyDTO {
    private String email;
    private Long surveyId;
}
