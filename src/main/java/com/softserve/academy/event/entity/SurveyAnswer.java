package com.softserve.academy.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "survey_answers")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Data
@NamedQuery(name="SurveyAnswer.findByQuestionId",
        query="from SurveyAnswer where question_id = :questionId ")
public class SurveyAnswer implements Serializable {

    private static final long serialVersionUID = -1003597080168505177L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne
    private SurveyQuestion question;

    @JoinColumn
    @ManyToOne
    private Contact contact;

    @Column
    private String value;

}
