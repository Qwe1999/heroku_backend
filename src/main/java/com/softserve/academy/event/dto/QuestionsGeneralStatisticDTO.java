package com.softserve.academy.event.dto;


import lombok.*;

import java.util.Set;

@Data
public class QuestionsGeneralStatisticDTO {
    String title;
    Set<OneQuestionGeneralStatisticDTO> questionDTOS;
}
