package com.softserve.academy.event.entity;

import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "survey_questions")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SurveyQuestion implements Serializable {

    private static final long serialVersionUID = -2673922858877977323L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Survey survey;

    @Column(nullable = false)
    private String question;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<SurveyAnswer> surveyAnswers = new HashSet<>();

    @Column
    @OrderBy
    private int index;

    @Enumerated(EnumType.STRING)
    private SurveyQuestionType type;

    @Column(nullable = false, length = 10000)
    private String choiceAnswers;

    private boolean required;
}
