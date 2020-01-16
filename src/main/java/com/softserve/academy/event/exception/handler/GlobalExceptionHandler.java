package com.softserve.academy.event.exception.handler;

import com.softserve.academy.event.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(Exception e, WebRequest request) {
        String description = request.getDescription(false);
        log.error("Not default exception", e);
        log.error("WebRequest description  : " + description);
        return new ResponseEntity<>(e.getMessage() + "\n" + description, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<Object> userNotFoundHandler(Exception e, WebRequest request) {
        return handler(e, request, "", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SurveyNotFound.class)
    public ResponseEntity<Object> surveyNotFoundHandler(Exception e, WebRequest request) {
        return handler(e, request, "", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedHandler(Exception e, WebRequest request) {
        return handler(e, request, "Try change other user information!", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IncorrectDataDB.class)
    public ResponseEntity<Object> incorrectDataDbHandler(Exception e, WebRequest request) {
        return handler(e, request, "Incorrect database info", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<Object> emailExistHandler(Exception e, WebRequest request) {
        return handler(e, request, "Try register already existing email", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> unauthorizedHandler(Exception e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SurveyNotBelongUser.class)
    public ResponseEntity<Object> surveyNotBelongUserHandler(Exception e, WebRequest request) {
        return handler(e, request, "Current survey is not belong to this user", HttpStatus.LOCKED);
    }

    @ExceptionHandler(IncorrectLinkException.class)
    public ResponseEntity<Object> incorrectLinkHandler(Exception e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SurveyAlreadyPassedException.class)
    public ResponseEntity<Object> surveyAlreadyPassedHandler(Exception e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.GONE);
    }

    private ResponseEntity<Object> handler(Exception e, WebRequest request, String message, HttpStatus status) {
        log.error(message, e);
        log.error(request.getDescription(false));
        return new ResponseEntity<>(e.getMessage(), status);
    }

}
