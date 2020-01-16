package com.softserve.academy.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SaveSurveyDTO {

    private String title;
    private String surveyPhotoName;
    private List<SurveyQuestionDTO> questions;

    public SaveSurveyDTO(List<SurveyQuestionDTO> questions){
        this.questions = questions;
    }
}