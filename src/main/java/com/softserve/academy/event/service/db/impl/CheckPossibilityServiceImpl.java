package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.exception.IncorrectLinkException;
import com.softserve.academy.event.service.db.CheckPossibilityService;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class CheckPossibilityServiceImpl implements CheckPossibilityService {

    @Override
    public String[] parseToken(String token) throws IncorrectLinkException {
        try {
            String[] res = new String(Base64.getDecoder().decode(token)).split(";");
            if (!res[0].matches(User.EMAIL_PATTERN)) {
                throw new IllegalArgumentException();
            }
            Long.parseLong(res[1]);
            return res;
        } catch (IllegalArgumentException e) {
            throw new IncorrectLinkException("Sorry, but you can`t pass survey by this link");
        }
    }
}
