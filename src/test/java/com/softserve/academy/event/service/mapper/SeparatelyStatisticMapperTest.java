package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.academy.event.dto.OneQuestionSeparatelyStatisticDTO;
import com.softserve.academy.event.dto.QuestionsSeparatelyStatisticDTO;
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

class SeparatelyStatisticMapperTest {

    private  static Survey survey;
    private SeparatelyStatisticMapper separatelyStatisticMapper;

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
        surveyAnswer.setValue("[\"1 p.m.\",\"3 p.m.\"]");
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
        separatelyStatisticMapper = Mappers.getMapper(SeparatelyStatisticMapper.class);
    }

    @ParameterizedTest
    @MethodSource("sourceToSetQuestionsDTO")
    void toSetQuestionsDTO(Survey survey,Set<QuestionsSeparatelyStatisticDTO> expected){

        Set<QuestionsSeparatelyStatisticDTO> dto =
                separatelyStatisticMapper.toSetQuestionsDTO(survey);
        System.out.println(dto);
        System.out.println(expected);
        assertEquals(expected,dto);
    }


    static Stream<Arguments> sourceToSetQuestionsDTO(){

        Set<QuestionsSeparatelyStatisticDTO> expected
                = new HashSet<>();


        QuestionsSeparatelyStatisticDTO questions =
                new QuestionsSeparatelyStatisticDTO();
        questions.setEmail("test1@gmail.com");
        Set<OneQuestionSeparatelyStatisticDTO> oneQuestionSet = new HashSet<>();
        OneQuestionSeparatelyStatisticDTO oneQuestion = new OneQuestionSeparatelyStatisticDTO();
        oneQuestion.setIndex(1);
        oneQuestion.setType(SurveyQuestionType.TEXTAREA);
        oneQuestion.setQuestion("What's your name?");
        oneQuestion.setChoiceAnswers(new ArrayList<>());
        oneQuestion.setAnswer(Arrays.asList("Vlad"));
        oneQuestionSet.add(oneQuestion);

        oneQuestion = new OneQuestionSeparatelyStatisticDTO();
        oneQuestion.setIndex(2);
        oneQuestion.setType(SurveyQuestionType.RADIOBUTTON);
        oneQuestion.setQuestion("What's time?");
        oneQuestion.setChoiceAnswers(Arrays.asList("1 a.m.","1 p.m.","3 p.m."));
        oneQuestion.setAnswer(Arrays.asList("1 p.m."));
        oneQuestionSet.add(oneQuestion);
        questions.setQuestionDTOS(oneQuestionSet);

        oneQuestion = new OneQuestionSeparatelyStatisticDTO();
        oneQuestion.setIndex(3);
        oneQuestion.setType(SurveyQuestionType.CHECKBOX);
        oneQuestion.setQuestion("What's day is today?");
        oneQuestion.setChoiceAnswers(Arrays.asList("Monday","Sunday","Saturday"));
        oneQuestion.setAnswer(Arrays.asList("Monday"));
        oneQuestionSet.add(oneQuestion);
        questions.setQuestionDTOS(oneQuestionSet);
        expected.add(questions);



        questions =
                new QuestionsSeparatelyStatisticDTO();
        questions.setEmail("test2@gmail.com");
        oneQuestionSet = new HashSet<>();
        oneQuestion = new OneQuestionSeparatelyStatisticDTO();
        oneQuestion.setIndex(1);
        oneQuestion.setType(SurveyQuestionType.TEXTAREA);
        oneQuestion.setQuestion("What's your name?");
        oneQuestion.setChoiceAnswers(new ArrayList<>());
        oneQuestion.setAnswer(Arrays.asList("Ivan"));
        oneQuestionSet.add(oneQuestion);

        oneQuestion = new OneQuestionSeparatelyStatisticDTO();
        oneQuestion.setIndex(2);
        oneQuestion.setType(SurveyQuestionType.RADIOBUTTON);
        oneQuestion.setQuestion("What's time?");
        oneQuestion.setChoiceAnswers(Arrays.asList("1 a.m.","1 p.m.","3 p.m."));
        oneQuestion.setAnswer(Arrays.asList("1 p.m.","3 p.m."));
        oneQuestionSet.add(oneQuestion);
        questions.setQuestionDTOS(oneQuestionSet);


        oneQuestion = new OneQuestionSeparatelyStatisticDTO();
        oneQuestion.setIndex(3);
        oneQuestion.setType(SurveyQuestionType.CHECKBOX);
        oneQuestion.setQuestion("What's day is today?");
        oneQuestion.setChoiceAnswers(Arrays.asList("Monday","Sunday","Saturday"));
        oneQuestion.setAnswer(Arrays.asList("Monday","Sunday"));
        oneQuestionSet.add(oneQuestion);
        questions.setQuestionDTOS(oneQuestionSet);
        expected.add(questions);

        return Stream.of(Arguments.of(survey,expected));
    }

    @Test
    void toQuestionsDTOWithoutQuestions(){
        Survey survey = new Survey();
        survey.setTitle("Survey #1");
        Set<QuestionsSeparatelyStatisticDTO> dto =
                separatelyStatisticMapper.toSetQuestionsDTO(survey);
        assertTrue(dto.isEmpty());
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
        Set<QuestionsSeparatelyStatisticDTO> actual;
        actual =separatelyStatisticMapper.toSetQuestionsDTO(survey);
       assertTrue(actual.isEmpty());
    }

    @Test
    void ifChoiceAnswersIsNotCorrect() {
        Survey survey = new Survey();
        Set<SurveyContact> surveyContacts = new HashSet<>();
        SurveyContact surveyContact = new SurveyContact();
        Contact contact = new Contact();
        surveyContact.setSurvey(survey);
        surveyContact.setContact(contact);
        surveyContacts.add(surveyContact);
        survey.setSurveyContacts(surveyContacts);
        survey.setTitle("Survey #1");
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        SurveyQuestion question = new SurveyQuestion();
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setChoiceAnswers("adsdsa");
        surveyQuestions.add(question);
        survey.setSurveyQuestions(surveyQuestions);
        assertThrows(IncorrectDataDB.class,() -> separatelyStatisticMapper.toSetQuestionsDTO(survey));
    }

    @Test
    void ifAnswerIsNotCorrect() {
        Survey survey = new Survey();
        Set<SurveyContact> surveyContacts = new HashSet<>();
        SurveyContact surveyContact = new SurveyContact();
        Contact contact = new Contact();
        contact.setId(1L);
        surveyContact.setSurvey(survey);
        surveyContact.setContact(contact);
        surveyContacts.add(surveyContact);
        survey.setSurveyContacts(surveyContacts);
        survey.setTitle("Survey #1");
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        SurveyQuestion question = new SurveyQuestion();
        Set<SurveyAnswer> surveyAnswers = new HashSet<>();
        SurveyAnswer surveyAnswer = new SurveyAnswer();
        surveyAnswer.setValue("asdzxc89");
        surveyAnswers.add(surveyAnswer);
        surveyAnswer.setContact(contact);
        question.setSurveyAnswers(surveyAnswers);
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setChoiceAnswers("[]");
        surveyQuestions.add(question);
        survey.setSurveyQuestions(surveyQuestions);
        assertThrows(IncorrectDataDB.class,() -> separatelyStatisticMapper.toSetQuestionsDTO(survey));
    }


    @Test
    void ifAnswerDoNotHaveContact() {
        Survey survey = new Survey();
        Set<SurveyContact> surveyContacts = new HashSet<>();
        SurveyContact surveyContact = new SurveyContact();
        Contact contact = new Contact();
        contact.setId(1L);
        surveyContact.setSurvey(survey);
        surveyContact.setContact(contact);
        surveyContacts.add(surveyContact);
        survey.setSurveyContacts(surveyContacts);
        survey.setTitle("Survey #1");
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        SurveyQuestion question = new SurveyQuestion();
        Set<SurveyAnswer> surveyAnswers = new HashSet<>();
        SurveyAnswer surveyAnswer = new SurveyAnswer();
        surveyAnswer.setValue("asdzxc89");
        surveyAnswers.add(surveyAnswer);
        question.setSurveyAnswers(surveyAnswers);
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setChoiceAnswers("[]");
        surveyQuestions.add(question);
        survey.setSurveyQuestions(surveyQuestions);
        assertThrows(NullPointerException.class,() -> separatelyStatisticMapper.toSetQuestionsDTO(survey));
    }

}
