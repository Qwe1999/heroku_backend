package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class OneQuestionGeneralStatisticDTO implements Serializable {

    private String question;
    private SurveyQuestionType type;
    private String[] choiceAnswers;
    private int index;
    private List<List<String>> answers;

}
