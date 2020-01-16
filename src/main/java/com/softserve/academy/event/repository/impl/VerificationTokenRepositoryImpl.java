package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.VerificationToken;
import com.softserve.academy.event.repository.VerificationTokenRepository;
import org.springframework.stereotype.Repository;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class VerificationTokenRepositoryImpl extends BasicRepositoryImpl<VerificationToken, Long> implements VerificationTokenRepository {

    @Override
    public VerificationToken findByToken(String token) {
        TypedQuery<VerificationToken> query = sessionFactory.getCurrentSession().createNamedQuery("findToken", VerificationToken.class).setMaxResults(1);
        query.setParameter("token", token);
        List<VerificationToken> vToken = query.getResultList();
        if (vToken.isEmpty()) {
            return null;
        }
        return vToken.get(0);
    }
}
