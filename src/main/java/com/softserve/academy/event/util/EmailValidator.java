package com.softserve.academy.event.util;

import com.softserve.academy.event.exception.IncorrectEmailsException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EmailValidator {

    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private EmailValidator(){}

    public static void validate(String[] list) {
        Set<ConstraintViolation<EmailAddress>> violations = new HashSet<>();
        for (EmailAddress address : Arrays.stream(list).map(EmailAddress::new).collect(Collectors.toList())) {
            violations.addAll(validator.validate(address));
        }
        if (!violations.isEmpty()) {
            throw new IncorrectEmailsException(violations.stream().map(e -> (String) e.getInvalidValue()).collect(Collectors.joining(",")));
        }
    }

}
