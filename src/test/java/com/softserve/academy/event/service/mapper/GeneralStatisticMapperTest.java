package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.OneQuestionGeneralStatisticDTO;
import com.softserve.academy.event.dto.QuestionsGeneralStatisticDTO;
import com.softserve.academy.event.entity.*;
import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import com.softserve.academy.event.exception.IncorrectDataDB;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class GeneralStatisticMapperTest {

    private GeneralStatisticMapper generalStatisticMapper;
    private static Survey survey;

    @BeforeAll
    static void initializeDTO(){
        survey = new Survey();
        survey.setTitle("Survey #1");
        Set<SurveyContact> contacts = new HashSet<>();
        SurveyContact surveyContact = new SurveyContact();
        Contact contact = new Contact();
        contact.setId(1L);
        contact.setEmail("test1@gmail.com");
        surveyContact.setContact(contact);
        surveyContact.setSurvey(survey);
        contacts.add(surveyContact);

        surveyContact = new SurveyContact();
        contact = new Contact();
        contact.setId(2L);
        contact.setEmail("test2@gmail.com");
        surveyContact.setContact(contact);
        surveyContact.setSurvey(survey);
        contacts.add(surveyContact);

        survey.setSurveyContacts(contacts);
        List<SurveyQuestion> questionList = new ArrayList<>();
        SurveyQuestion question = new SurveyQuestion();
        question.setChoiceAnswers("[]");
        question.setIndex(1);
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setQuestion("What's your name?");
        Set<SurveyAnswer> answerSet = new HashSet<>();

        Iterator<SurveyContact> contactIterator = contacts.iterator();
        SurveyAnswer surveyAnswer = new SurveyAnswer();
        surveyAnswer.setId(1L);
        surveyAnswer.setContact(contactIterator.next().getContact());
        surveyAnswer.setValue("[\"Vlad\"]");
        answerSet.add(surveyAnswer);

        surveyAnswer = new SurveyAnswer();
        surveyAnswer.setId(2L);
        surveyAnswer.setContact(contactIterator.next().getContact());
        surveyAnswer.setValue("[\"Ivan\"]");
        answerSet.add(surveyAnswer);


        question.setSurveyAnswers(answerSet);
        questionList.add(question);


        question = new SurveyQuestion();
        question.setChoiceAnswers("[\"1 a.m.\",\"1 p.m.\",\"3 p.m.\"]");
        question.setIndex(2);
        question.setType(SurveyQuestionType.RADIOBUTTON);
        question.setQuestion("What's time?");
        answerSet = new HashSet<>();


        contactIterator = contacts.iterator();
        surveyAnswer = new SurveyAnswer();
        surveyAnswer.setId(1L);
        surveyAnswer.setContact(contactIterator.next().getContact());
        surveyAnswer.setValue("[\"1 p.m.\"]");
        answerSet.add(surveyAnswer);

        surveyAnswer = new SurveyAnswer();
        surveyAnswer.setId(2L);
        surveyAnswer.setContact(contactIterator.next().getContact());
        surveyAnswer.setValue("[\"3 p.m.\"]");
        answerSet.add(surveyAnswer);

        question.setSurveyAnswers(answerSet);
        questionList.add(question);


        question = new SurveyQuestion();
        question.setChoiceAnswers("[\"Monday\",\"Sunday\",\"Saturday\"]");
        question.setIndex(3);
        question.setType(SurveyQuestionType.CHECKBOX);
        question.setQuestion("What's day is today?");
        answerSet = new HashSet<>();


        contactIterator = contacts.iterator();
        surveyAnswer = new SurveyAnswer();
        surveyAnswer.setId(1L);
        surveyAnswer.setContact(contactIterator.next().getContact());
        surveyAnswer.setValue("[\"Monday\"]");
        answerSet.add(surveyAnswer);


        surveyAnswer = new SurveyAnswer();
        surveyAnswer.setId(2L);
        surveyAnswer.setContact(contactIterator.next().getContact());
        surveyAnswer.setValue("[\"Monday\",\"Sunday\"]");
        answerSet.add(surveyAnswer);

        question.setSurveyAnswers(answerSet);
        questionList.add(question);

        survey.setSurveyQuestions(questionList);
    }

    @BeforeEach
    void setUp() {
        generalStatisticMapper = Mappers.getMapper(GeneralStatisticMapper.class);
    }

    @ParameterizedTest
    @MethodSource("sourceToQuestionsDTO")
    void toQuestionsDTO(Survey survey,QuestionsGeneralStatisticDTO expected)  {
        QuestionsGeneralStatisticDTO actual =
                generalStatisticMapper.toQuestionsDTO(survey);
        assertEquals(actual,expected);
    }

    static Stream<Arguments> sourceToQuestionsDTO(){
        QuestionsGeneralStatisticDTO expected =
                new QuestionsGeneralStatisticDTO();
        expected.setTitle("Survey #1");
        Set<OneQuestionGeneralStatisticDTO> oneQuestionsSet = new HashSet<>();
        OneQuestionGeneralStatisticDTO oneQuestion = new OneQuestionGeneralStatisticDTO();
        oneQuestion.setIndex(1);
        oneQuestion.setChoiceAnswers(new ArrayList<>());
        oneQuestion.setType(SurveyQuestionType.TEXTAREA);
        oneQuestion.setQuestion("What's your name?");
        List<List<String >> answers = new ArrayList<>();
        answers.add(Arrays.asList("Vlad"));
        answers.add(Arrays.asList("Ivan"));
        oneQuestion.setAnswers(answers);
        oneQuestionsSet.add(oneQuestion);

        oneQuestion = new OneQuestionGeneralStatisticDTO();
        oneQuestion.setIndex(2);
        oneQuestion.setChoiceAnswers(Arrays.asList("1 a.m.","1 p.m.","3 p.m."));
        oneQuestion.setType(SurveyQuestionType.RADIOBUTTON);
        oneQuestion.setQuestion("What's time?");
        answers = new ArrayList<>();
        answers.add(Arrays.asList("1 p.m."));
        answers.add(Arrays.asList("3 p.m."));
        oneQuestion.setAnswers(answers);
        oneQuestionsSet.add(oneQuestion);

        oneQuestion = new OneQuestionGeneralStatisticDTO();
        oneQuestion.setIndex(3);
        oneQuestion.setChoiceAnswers(Arrays.asList("Monday","Sunday","Saturday"));
        oneQuestion.setType(SurveyQuestionType.CHECKBOX);
        oneQuestion.setQuestion("What's day is today?");
        answers = new ArrayList<>();
        answers.add(Arrays.asList("Monday"));
        answers.add(Arrays.asList("Monday","Sunday"));
        oneQuestion.setAnswers(answers);
        oneQuestionsSet.add(oneQuestion);

        expected.setQuestionDTOS(oneQuestionsSet);

        return Stream.of(Arguments.of(survey,expected));
    }

    @Test
    void toQuestionsDTOWithoutQuestions() {
        Survey survey = new Survey();
        survey.setTitle("Survey #1");
        QuestionsGeneralStatisticDTO dto =
                generalStatisticMapper.toQuestionsDTO(survey);
        assertTrue(dto.getQuestionDTOS().isEmpty());
    }

    @Test
    void toQuestionsDTOWithoutAnswers(){
        Survey survey = new Survey();
        survey.setTitle("Survey #1");
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        SurveyQuestion question = new SurveyQuestion();
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setIndex(1);
        question.setChoiceAnswers("[]");
        question.setQuestion("");
        surveyQuestions.add(question);
        Set<SurveyAnswer> surveyAnswers = new HashSet<>();
        question.setSurveyAnswers(surveyAnswers);
        survey.setSurveyQuestions(surveyQuestions);

        QuestionsGeneralStatisticDTO dto = generalStatisticMapper.toQuestionsDTO(survey);
        assertEquals("Survey #1",dto.getTitle());

        Set<OneQuestionGeneralStatisticDTO> oneQuestionSetExpected = new HashSet<>();
        OneQuestionGeneralStatisticDTO oneQuestion = new OneQuestionGeneralStatisticDTO();
        oneQuestion.setAnswers(new ArrayList<>());
        oneQuestion.setIndex(1);
        oneQuestion.setQuestion("");
        oneQuestion.setType(SurveyQuestionType.TEXTAREA);
        oneQuestion.setChoiceAnswers(new ArrayList<>());
        oneQuestionSetExpected.add(oneQuestion);
        assertArrayEquals(oneQuestionSetExpected.toArray(),dto.getQuestionDTOS().toArray());
    }

    @Test
    void ifChoiceAnswersIsNotCorrect(){
        Survey survey = new Survey();
        survey.setTitle("Survey #1");
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        SurveyQuestion question = new SurveyQuestion();
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setChoiceAnswers("adsdsa");
        surveyQuestions.add(question);
        survey.setSurveyQuestions(surveyQuestions);
        assertThrows(IncorrectDataDB.class,() -> generalStatisticMapper.toQuestionsDTO(survey));
    }

    @Test
    void ifAnswerIsNotCorrect(){
        Survey survey = new Survey();
        survey.setTitle("Survey #1");
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        SurveyQuestion question = new SurveyQuestion();
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setIndex(1);
        question.setChoiceAnswers("[]");
        question.setQuestion("");
        surveyQuestions.add(question);
        Set<SurveyAnswer> surveyAnswers = new HashSet<>();
        SurveyAnswer surveyAnswer = new SurveyAnswer();
        surveyAnswer.setValue("asdqw");
        surveyAnswers.add(surveyAnswer);
        question.setSurveyAnswers(surveyAnswers);
        survey.setSurveyQuestions(surveyQuestions);


        assertThrows(IncorrectDataDB.class,() -> generalStatisticMapper.toQuestionsDTO(survey));
    }


}





























