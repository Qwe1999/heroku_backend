package com.softserve.academy.event.exception;

public class QuestionNotFoundException extends  RuntimeException{

    public QuestionNotFoundException(String message) {
        super(message);
    }
}
