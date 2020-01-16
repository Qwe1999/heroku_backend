package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;

import java.math.BigInteger;
import java.util.Optional;

public interface SurveyRepository extends BasicRepository<Survey, Long> {

    Page<Survey> findAllByPageableAndUserEmail(Pageable pageable, String userEmail);

    Page<Survey> findAllByPageableAndStatusAndUserEmail(Pageable pageable, SurveyStatus status, String userEmail);

    Optional<BigInteger> cloneSurvey(DuplicateSurveySettings settings);

}
