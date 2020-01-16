package com.softserve.academy.event.controller;

import com.softserve.academy.event.service.db.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ResolvableType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@PropertySource("classpath:application.properties")
@Slf4j
public class SocialLoginController {

    private static final String AUTHORIZATION_REQUEST_BASE_URI = "oauth2/authorize-client";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

    @Value("${app.frontend.url}")
    private String frontUrl;

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
            clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(),
                    AUTHORIZATION_REQUEST_BASE_URI + "/" + registration.getRegistrationId()));
        }

        return ResponseEntity.ok(oauth2AuthenticationUrls);
    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(OAuth2AuthenticationToken authentication, HttpServletResponse httpServletResponse) {

        userService.newSocialUser(authentication.getPrincipal());

        try {
            httpServletResponse.sendRedirect(frontUrl + "/login");
            return "login success";
        } catch (IOException e) {
            log.error("Redirect error", e);
            return "login failed";
        }
    }

    @GetMapping(value = "/authenticatedEmail")
    public String getAuthenticatedEmail() {
        return userService.getAuthenticatedUserEmail();
    }
}
