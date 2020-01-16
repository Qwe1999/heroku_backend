package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.enums.SurveyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyDTO {

    private Long id;
    private String title;
    private SurveyStatus status;
    private String imageUrl;
    private Long countAnswers;
    private Long countContacts;

}
