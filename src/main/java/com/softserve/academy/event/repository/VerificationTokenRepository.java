package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.VerificationToken;

public interface VerificationTokenRepository extends BasicRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
