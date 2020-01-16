package com.softserve.academy.event.dto;

import lombok.*;

import java.util.Set;

@Data
public class QuestionsSeparatelyStatisticDTO {
    private String email;
    private Set<OneQuestionSeparatelyStatisticDTO> questionDTOS;

}
