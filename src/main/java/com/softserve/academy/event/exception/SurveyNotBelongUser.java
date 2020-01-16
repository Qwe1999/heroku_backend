package com.softserve.academy.event.exception;

public class SurveyNotBelongUser extends RuntimeException {

    public SurveyNotBelongUser() {
        super("Survey not belongs user");
    }
}
