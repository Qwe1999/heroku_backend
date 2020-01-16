package com.softserve.academy.event.entity.enums;

import lombok.Getter;

@Getter
public enum SurveyStatus {
    NON_ACTIVE(0),
    ACTIVE(1),
    DONE(2),
    TEMPLATE(3);

    private final int number;

    SurveyStatus(int number){
        this.number = number;
    }

}
