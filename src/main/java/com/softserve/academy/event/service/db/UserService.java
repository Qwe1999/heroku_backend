package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.VerificationToken;
import com.softserve.academy.event.entity.enums.TokenValidation;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

public interface UserService extends BasicService<User, Long> {
    Optional<Long> getAuthenticationId();

    VerificationToken updateTokenExpiration(String token);

    User newUserAccount(User account);

    VerificationToken createVerificationToken(User user);

    TokenValidation validateVerificationToken(String token);

    User newSocialUser(OAuth2User oAuth2User);

    String getAuthenticatedUserEmail();
}
