package com.softserve.academy.event.exception;

public class UserNotFound extends RuntimeException {

    public UserNotFound() {
        super("No such user");
    }

}
