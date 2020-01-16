package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.ContactResponseDTO;
import com.softserve.academy.event.dto.QuestionDTO;
import com.softserve.academy.event.dto.SurveyContactDTO;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.service.db.AnswerService;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.db.QuestionService;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import com.softserve.academy.event.service.mapper.AnswerMapper;
import com.softserve.academy.event.service.mapper.QuestionMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(value = "/question")
@RestController
@RequestMapping("question")
public class QuestionController {

    private final ContactService contactService;
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;
    private final AnswerService answerService;
    private final SurveyContactConnectorService surveyContactConnectorService;
    private final AnswerMapper answerMapper;

    @Autowired
    public QuestionController(AnswerMapper answerMapper, ContactService contactService, QuestionService questionService, QuestionMapper questionMapper, AnswerService answerService, SurveyContactConnectorService surveyContactConnectorService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.surveyContactConnectorService = surveyContactConnectorService;
        this.contactService = contactService;
        this.questionMapper = questionMapper;
        this.answerMapper = answerMapper;
    }

    @ApiOperation(value = "Get a survey form for contact", notes = "Checks an e-mail, Id(survey) and builds a form", response = SurveyContactDTO.class)
    @GetMapping
    public ResponseEntity<SurveyContactDTO> startSurvey(Long surveyId, String contactEmail){
        if (!contactService.canPass(surveyId, contactEmail))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        List<QuestionDTO> questionsDTO = questionMapper.listQuestionToDTO(questionService.findBySurveyId(surveyId));
        SurveyContactDTO dto = new SurveyContactDTO();
        dto.setContactEmail(contactEmail);
        dto.setSurveyId(surveyId);
        dto.setQuestions(questionsDTO);
        if(questionsDTO.isEmpty())
            return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @ApiOperation(value = "Save answers to the database")
    @PostMapping
    public ResponseEntity<String> addAnswers(@RequestBody ContactResponseDTO contactResponseDTO){
        String result;
        Optional<Long> contactId = contactService.getIdByEmail(contactResponseDTO.getContactEmail());
        if(!contactId.isPresent()) {
            result = "missing email data";
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        Optional <SurveyContact> surveyContact =
                surveyContactConnectorService.findByContactAndSurvey(contactId.get(), contactResponseDTO.getSurveyId());
        if(!surveyContact.isPresent()){
            result = "mail is not in the invite list";
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        surveyContact.get().setCanPass(false);
        surveyContactConnectorService.update(surveyContact.get());
        contactResponseDTO.getAnswers().stream()
                .peek(answerDTO -> answerDTO.setContactId(contactId.get()))
                .map(answerMapper::toEntity)
                .forEach(answerService::save);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
