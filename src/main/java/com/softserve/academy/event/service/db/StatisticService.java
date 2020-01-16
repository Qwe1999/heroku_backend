package com.softserve.academy.event.service.db;

import com.softserve.academy.event.dto.QuestionsSeparatelyStatisticDTO;
import com.softserve.academy.event.dto.QuestionsGeneralStatisticDTO;

import java.util.Set;

public interface StatisticService {

    QuestionsGeneralStatisticDTO getGeneralStatistic(Long surveyId);

    Set<QuestionsSeparatelyStatisticDTO> getSeparatelyStatistic(Long surveyId);

    void isSurveyBelongsUser(Long surveyId);
}
