package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.VerificationToken;
import com.softserve.academy.event.exception.EmailExistException;
import com.softserve.academy.event.entity.enums.TokenValidation;

import java.util.Optional;

public interface UserService extends BasicService<User, Long> {
    Optional<Long> getAuthenicationId();
    VerificationToken generateNewVerificationToken(String token);
    User newUserAccount(User account) throws EmailExistException;
    User getUser(String verificationToken);
    void createVerificationToken(User user, String token);
    TokenValidation validateVerificationToken(String token);
}
