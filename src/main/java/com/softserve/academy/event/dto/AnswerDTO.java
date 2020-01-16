package com.softserve.academy.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDTO {

    private Long questionId;
    private Long contactId;
    private Object answers;
}