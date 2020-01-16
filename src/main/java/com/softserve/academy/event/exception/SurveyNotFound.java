package com.softserve.academy.event.exception;

public class SurveyNotFound extends RuntimeException {

    public SurveyNotFound() {
        super("No such survey");
    }

}
