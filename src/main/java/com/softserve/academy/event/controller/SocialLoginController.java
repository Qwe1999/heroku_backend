package com.softserve.academy.event.controller;

import com.softserve.academy.event.entity.UserSocial;
import com.softserve.academy.event.entity.enums.OauthType;
import com.softserve.academy.event.service.db.UserSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    private final UserSocialService userSocialService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public SocialLoginController(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService, UserSocialService userSocialService) {
        this.userSocialService = userSocialService;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/oauth_login")
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
        OAuth2User oAuth2User = authentication.getPrincipal();

        UserSocial userSocial = new UserSocial();
        userSocial.setType(OauthType.valueOf(authentication.getAuthorizedClientRegistrationId().toUpperCase()));
        userSocial.setNickname(oAuth2User.getAttribute("name"));
        userSocial.setEmail(oAuth2User.getAttribute("email"));
        switch (userSocial.getType()){
            case FACEBOOK:{
                userSocial.setSocialId(oAuth2User.getAttribute("id"));
                break;
            }
            case GOOGLE:{
                userSocial.setSocialId(oAuth2User.getAttribute("sub"));
                break;
            }
        }
        try {
            userSocialService.save(userSocial);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        try {
            httpServletResponse.sendRedirect("http://localhost:4200/surveys");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "loginSuccess";
    }

    @GetMapping(value = "/test")
    public String test(){
//        return ((DefaultOidcUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();                        // for google
        return ((DefaultOAuth2User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAttribute("email");    // for facebook
    }
}