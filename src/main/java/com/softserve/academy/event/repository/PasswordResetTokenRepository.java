package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.PasswordResetToken;
import com.softserve.academy.event.entity.User;

public interface PasswordResetTokenRepository extends BasicRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);
}
