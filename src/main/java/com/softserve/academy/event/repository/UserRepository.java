package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.User;
import java.util.Optional;


public interface UserRepository extends BasicRepository<User, Long> {
    Optional<User> findByEmail(String email);
}


