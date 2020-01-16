package com.softserve.academy.event.service.db;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}



