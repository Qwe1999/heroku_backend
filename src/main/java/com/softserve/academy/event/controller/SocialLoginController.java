package com.softserve.academy.event.controller;

import com.softserve.academy.event.service.db.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:4200")
public class SocialLoginController {

    private static final String authorizationRequestBaseUri = "oauth2/authorize-client";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final UserService userService;

    @Autowired
    public SocialLoginController(ClientRegistrationRepository clientRegistrationRepository, UserService userService) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.userService = userService;
    }

    @GetMapping("/oauth_login")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, String>> getLoginPage() {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        if (clientRegistrations != null) {
            clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        }

        return ResponseEntity.ok(oauth2AuthenticationUrls);
    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(OAuth2AuthenticationToken authentication, HttpServletResponse httpServletResponse) {

        userService.newSocialUser(authentication.getPrincipal());

        try {
            httpServletResponse.sendRedirect("http://localhost:4200/surveys");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "loginSuccess";
    }

    @GetMapping(value = "/test")
    public String test() {

        Object something = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (something instanceof DefaultOidcUser) {
            return ((DefaultOidcUser)something).getEmail();                     // for google
        } else if (something instanceof DefaultOAuth2User) {
            return ((DefaultOAuth2User)something).getAttribute("email"); // for facebook
        } else {
            return something.toString();
        }
    }
}