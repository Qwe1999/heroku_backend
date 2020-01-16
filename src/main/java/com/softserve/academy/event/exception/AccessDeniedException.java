package com.softserve.academy.event.exception;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super("User have no access to this resources");
    }

}
