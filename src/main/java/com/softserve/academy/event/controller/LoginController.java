package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.UserDto;
import com.softserve.academy.event.entity.enums.TokenValidation;
import com.softserve.academy.event.exception.EmailExistException;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public LoginController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

   @PostMapping(value = "/registration")
    public ResponseEntity registerUserAccount(@RequestBody UserDto accountDto) {
       try {
           UserDto registered = userMapper.userToDto(userService.newUserAccount(userMapper.userDtoToUser(accountDto)));
           userService.createVerificationToken(userMapper.userDtoToUser(registered));
           return new ResponseEntity<>(HttpStatus.CREATED);
       }catch (EmailExistException e) {
           return new ResponseEntity<>(e, HttpStatus.CONFLICT);
       }
    }

    @GetMapping(value = "/registrationConfirm")
    public ResponseEntity confirmRegistration(@RequestParam("token")String token)  {
        TokenValidation result = userService.validateVerificationToken(token);
        if (TokenValidation.TOKEN_VALID.equals(result)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/resendRegistrationToken")
    public ResponseEntity resendRegistrationToken( @RequestParam("token") String existingToken) {
        userService.updateTokenExpiration(existingToken);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/login")
    public ResponseEntity getLogin() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
