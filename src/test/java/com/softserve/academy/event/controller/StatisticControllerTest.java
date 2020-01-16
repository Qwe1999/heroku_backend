package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.OneQuestionGeneralStatisticDTO;
import com.softserve.academy.event.dto.OneQuestionSeparatelyStatisticDTO;
import com.softserve.academy.event.dto.QuestionsGeneralStatisticDTO;
import com.softserve.academy.event.dto.QuestionsSeparatelyStatisticDTO;
import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import com.softserve.academy.event.exception.SurveyNotFound;
import com.softserve.academy.event.service.db.StatisticService;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig({StatisticControllerTest.Config.class, ApplicationInitializerTest.class})
class StatisticControllerTest {

    @Configuration
    @Import(StatisticController.class)
    @EnableWebMvc
    static class Config {
        @Bean
        public StatisticService statisticService() {
            return Mockito.mock(StatisticService.class);
        }
    }

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Autowired
    private StatisticService statisticService;

    @ParameterizedTest
    @MethodSource("sourceGetGeneralStatistic")
    void getGeneralStatistic(QuestionsGeneralStatisticDTO questionsDTO) throws Exception {
        when(statisticService.getGeneralStatistic(1L)).thenReturn(questionsDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/statistic/general?surveyId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionDTOS",
                        hasSize(1)))
                .andExpect(jsonPath("$.title", is(questionsDTO.getTitle())))
                .andExpect(jsonPath("$.questionDTOS",hasSize(not(0))));
    }


    static Stream<QuestionsGeneralStatisticDTO> sourceGetGeneralStatistic()  {
        List<QuestionsGeneralStatisticDTO> data =  new ArrayList<>();
        QuestionsGeneralStatisticDTO generalStatisticDTO1 = new QuestionsGeneralStatisticDTO();
        generalStatisticDTO1.setTitle("Survey #1");
        Set<OneQuestionGeneralStatisticDTO> setQuestions = new HashSet<>();
        OneQuestionGeneralStatisticDTO question = new OneQuestionGeneralStatisticDTO();
        question.setIndex(1);
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setQuestion("What's your first name?");
        question.setChoiceAnswers(new ArrayList<>());
        List<List<String>> answers = new ArrayList<>();
        answers.add(Collections.singletonList("Vlad"));
        answers.add(Collections.singletonList("Ivan"));
        answers.add(Collections.singletonList("Tolik"));
        question.setAnswers(answers);
        setQuestions.add(question);
        generalStatisticDTO1.setQuestionDTOS(setQuestions);
        data.add(generalStatisticDTO1);

        QuestionsGeneralStatisticDTO generalStatisticDTO2 = new QuestionsGeneralStatisticDTO();
        generalStatisticDTO1.setTitle("Survey #2");
        setQuestions = new HashSet<>();
        question = new OneQuestionGeneralStatisticDTO();
        question.setIndex(1);
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setQuestion("What's your last name?");
        question.setChoiceAnswers(new ArrayList<>());
        answers = new ArrayList<>();
        answers.add(Collections.singletonList("Banar"));
        answers.add(Collections.singletonList("Rospopov"));
        answers.add(Collections.singletonList("Roscolkov"));
        question.setAnswers(answers);
        setQuestions.add(question);
        generalStatisticDTO2.setQuestionDTOS(setQuestions);
        data.add(generalStatisticDTO2);


        return data.stream();
    }

    @ParameterizedTest
    @MethodSource("sourceGetSeparatelyStatistic")
    void getSeparatelyStatistic(Set<QuestionsSeparatelyStatisticDTO> setQuestionsDTO) throws Exception {
        when(statisticService.getSeparatelyStatistic(1L)).thenReturn(setQuestionsDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/statistic/separately?surveyId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].questionDTOS",hasSize(not(0))))
                .andExpect(jsonPath("$[0].email",
                        is(setQuestionsDTO.iterator().next().getEmail())));


    }

    static Stream<Set<QuestionsSeparatelyStatisticDTO>> sourceGetSeparatelyStatistic() {
        List<Set<QuestionsSeparatelyStatisticDTO>> data = new ArrayList<>();

        Set<QuestionsSeparatelyStatisticDTO> setQuestions = new HashSet<>();
        QuestionsSeparatelyStatisticDTO questions = new QuestionsSeparatelyStatisticDTO();
        questions.setEmail("test1@gmail.com");

        Set<OneQuestionSeparatelyStatisticDTO> setOneQuestion = new HashSet<>();
        OneQuestionSeparatelyStatisticDTO question = new OneQuestionSeparatelyStatisticDTO();
        question.setIndex(1);
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setQuestion("What's your last name?");
        question.setChoiceAnswers(new ArrayList<>());
        question.setAnswer(Collections.singletonList("Vlad"));
        setOneQuestion.add(question);
        questions.setQuestionDTOS(setOneQuestion);
        setQuestions.add(questions);


        questions = new QuestionsSeparatelyStatisticDTO();
        questions.setEmail("test2@gmail.com");
        setOneQuestion = new HashSet<>();
        question = new OneQuestionSeparatelyStatisticDTO();
        question.setIndex(1);
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setQuestion("What's your last name?");
        question.setChoiceAnswers(new ArrayList<>());
        question.setAnswer(Collections.singletonList("Vlad"));
        setOneQuestion.add(question);
        questions.setQuestionDTOS(setOneQuestion);
        setQuestions.add(questions);

        data.add(setQuestions);
        return data.stream();
    }


}
