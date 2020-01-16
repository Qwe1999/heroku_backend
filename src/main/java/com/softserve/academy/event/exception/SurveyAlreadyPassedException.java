package com.softserve.academy.event.exception;

public class SurveyAlreadyPassedException extends Exception {
    public SurveyAlreadyPassedException() {
        super();
    }

    public SurveyAlreadyPassedException(String message) {
        super(message);
    }
}
