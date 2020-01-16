package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.QuestionsSeparatelyStatisticDTO;
import com.softserve.academy.event.dto.QuestionsGeneralStatisticDTO;
import com.softserve.academy.event.service.db.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/statistic")
@Slf4j
public class StatisticController {

    private StatisticService statisticService;
    private static final String RETURN_ID = "return %s for surveyId = %d";


    @Autowired
    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/general")
    public ResponseEntity<QuestionsGeneralStatisticDTO> getGeneralStatistic(
            @RequestParam(name = "surveyId") long surveyId) {
        log.info("call with surveyId = " + surveyId);

        statisticService.isSurveyBelongsUser(surveyId);
        QuestionsGeneralStatisticDTO surveyDTO =
                statisticService.getGeneralStatistic(surveyId);
        log.info(String.format(RETURN_ID, HttpStatus.OK, surveyId));
        return new ResponseEntity<>(surveyDTO, HttpStatus.OK);
    }

    @GetMapping("/separately")
    public ResponseEntity<Set<QuestionsSeparatelyStatisticDTO>> getSeparatelyStatistic(
            @RequestParam(name = "surveyId") long surveyId) {
        log.info("call with surveyId = " + surveyId);

        statisticService.isSurveyBelongsUser(surveyId);
        Set<QuestionsSeparatelyStatisticDTO> surveyDTO =
                statisticService.getSeparatelyStatistic(surveyId);
        log.info(String.format(RETURN_ID, HttpStatus.OK, surveyId));
        return new ResponseEntity<>(surveyDTO, HttpStatus.OK);

    }

}
