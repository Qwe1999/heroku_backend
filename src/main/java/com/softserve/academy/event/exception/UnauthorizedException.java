package com.softserve.academy.event.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("Unauthorized user");
    }
}
