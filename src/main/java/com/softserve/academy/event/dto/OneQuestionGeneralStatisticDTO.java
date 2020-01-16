package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
public class OneQuestionGeneralStatisticDTO implements Serializable {

    private String question;
    private SurveyQuestionType type;
    private List<String> choiceAnswers;
    private int index;
    private List<List<String>> answers;

}
