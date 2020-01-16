package com.softserve.academy.event.service.db;

import com.softserve.academy.event.exception.IncorrectLinkException;

public interface CheckPossibilityService {

    public String[] parseToken(String token) throws IncorrectLinkException;
}
