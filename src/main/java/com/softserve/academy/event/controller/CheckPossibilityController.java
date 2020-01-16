package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.CheckPossibilityDTO;
import com.softserve.academy.event.dto.ContactSurveyDTO;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;
import com.softserve.academy.event.service.db.CheckPossibilityService;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Optional;

@Api(value = "/testAccess")
@RestController
@RequestMapping(value = "/testAccess")
@Slf4j
public class CheckPossibilityController {
    private final SurveyContactConnectorService surveyContactConnectorService;
    private final CheckPossibilityService checkPossibilityService;
    private final ContactService contactService;

    @Autowired
    public CheckPossibilityController(SurveyContactConnectorService surveyContactConnectorService, ContactService contactService, CheckPossibilityService checkPossibilityService) {
        this.surveyContactConnectorService = surveyContactConnectorService;
        this.checkPossibilityService = checkPossibilityService;
        this.contactService = contactService;
    }

    @ApiOperation(value = "Mail verification")
    @GetMapping(value = "/{token}")
    public ResponseEntity<String> mailTest(@PathVariable(name = "token") String token) throws IncorrectLinkException, SurveyAlreadyPassedException {
        String[] res = checkPossibilityService.parseToken(token);
        Optional<Long> longOptional = contactService.getIdByEmail(res[0]);
        if (!longOptional.isPresent()) {
            log.error("This survey is not available for the current user");
            return new ResponseEntity<>("Sorry, but you can`t pass survey by this link", HttpStatus.UNAUTHORIZED);
        }
        if (surveyContactConnectorService.isEnable(longOptional.get(), Long.valueOf(res[1]))) {
            return ResponseEntity.ok(token);
        }
        log.error("This user has already passed survey");
        return new ResponseEntity<>("Sorry, but you can`t pass survey by this link", HttpStatus.UNAUTHORIZED);
    }

    @ApiOperation(value = "Check e-mail")
    @PostMapping(value = "/check")
    public ResponseEntity<ContactSurveyDTO> enterEmail(@RequestBody CheckPossibilityDTO checkPossibilityDTO) {
        String[] strings = new String(Base64.getDecoder().decode(checkPossibilityDTO.getToken())).split(";");
        final ContactSurveyDTO dto = new ContactSurveyDTO(strings[0], Long.valueOf(strings[1]));
        if (strings[0].matches(User.EMAIL_PATTERN)
                && checkPossibilityDTO.getEmail().matches(User.EMAIL_PATTERN)
                && strings[0].equals(checkPossibilityDTO.getEmail())) {
            return ResponseEntity.ok(dto);
        }
        log.error("Email does not match the specified contact");
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
