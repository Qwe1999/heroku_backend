package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import lombok.*;

import java.util.List;

@Data
public class OneQuestionSeparatelyStatisticDTO {
    private String question;
    private SurveyQuestionType type;
    private List<String> choiceAnswers;
    private int index;
    private List<String> answer;


}
