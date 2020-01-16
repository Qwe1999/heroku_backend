package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.VerificationToken;
import com.softserve.academy.event.entity.enums.Roles;
import com.softserve.academy.event.entity.enums.TokenValidation;
import com.softserve.academy.event.exception.EmailExistException;
import com.softserve.academy.event.exception.UserNotFound;
import com.softserve.academy.event.repository.UserRepository;
import com.softserve.academy.event.repository.VerificationTokenRepository;
import com.softserve.academy.event.service.db.EmailService;
import com.softserve.academy.event.service.db.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@PropertySource("classpath:application.properties")
public class UserServiceImpl implements UserService {

    @Value("${app.frontend.url}")
    private String frontUrl;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final VerificationTokenRepository tokenRepository;

    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, VerificationTokenRepository tokenRepository, @Lazy EmailService emailService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    @Override
    public Optional<Long> getAuthenticationId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            Long id = userRepository.findByEmail(email).orElseThrow(UserNotFound::new).getId();
            return Optional.of(id);
        }
        return Optional.empty();
    }

    @Override
    public VerificationToken updateTokenExpiration(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        verificationToken.updateToken(UUID.randomUUID().toString());
        emailConfirm(verificationToken.getUser().getEmail(), verificationToken.getToken());
        return tokenRepository.save(verificationToken);
    }

    @Override
    public User newUserAccount(User userAccount) {
        if (emailExists(userAccount.getEmail())) {
            throw new EmailExistException("There is an account with that email address: " + userAccount.getEmail());
        }
        User user = new User();
        user.setEmail(userAccount.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(Objects.requireNonNull(userAccount.getPassword())));
        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public VerificationToken createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken vToken = new VerificationToken(token, user);
        emailConfirm(user.getEmail(), token);
        return tokenRepository.save(vToken);
    }

    @Override
    public TokenValidation validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TokenValidation.TOKEN_INVALID;
        }

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return TokenValidation.TOKEN_EXPIRED;
        }
        final User user = verificationToken.getUser();
        user.setActive(true);
        userRepository.save(user);
        return TokenValidation.TOKEN_VALID;
    }

    private void emailConfirm(String email, String token) {
        String subject = "Registration Confirmation";
        String confirmationUrl = frontUrl + "/confirm?token=" + token;
        String message = "Thank you for registration. Please click on the below link to activate your account.";
        emailService.sendMail(email,subject,message + confirmationUrl);
    }

    @Override
    public Optional<User> findFirstById(Long id) {
        return userRepository.findFirstById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public User update(User object) {
        return userRepository.update(object);
    }

    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public User newSocialUser(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");

        if (!emailExists(email)) {
            User user = new User();
            user.setRole(Roles.USER);
            user.setActive(true);
            user.setEmail(email);
            user.setContacts(new HashSet<>());
            user.setCreationDate(LocalDate.now());
            user.setPassword(null);
            user.setSurveys(new HashSet<>());

            return save(user);
        }
        return userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
    }
    public String getAuthenticatedUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();                      // for our login
        } else if (principal instanceof DefaultOidcUser) {
            return ((DefaultOidcUser) principal).getEmail();                     // for google
        } else if (principal instanceof DefaultOAuth2User) {
            return ((DefaultOAuth2User) principal).getAttribute("email"); // for facebook
        } else {
            return null;
        }
    }
}
