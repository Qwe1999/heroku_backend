package com.softserve.academy.event.util;

import com.softserve.academy.event.exception.UnauthorizedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class SecurityUserUtil {

    private SecurityUserUtil() {
    }

    public static boolean checkUserEmailNotEqualsCurrentUserEmail(String email) {
        return !email.equals(getCurrentUserEmail());
    }

    public static String getCurrentUserEmail() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails) userDetails).getUsername();
        } else if (userDetails instanceof DefaultOidcUser) {
            return ((DefaultOidcUser) userDetails).getEmail();                     // for google
        } else if (userDetails instanceof DefaultOAuth2User) {
            return ((DefaultOAuth2User) userDetails).getAttribute("email"); // for facebook
        } else {
            throw new UnauthorizedException();
        }
    }

}
