package com.softserve.academy.event.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "surveys")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(of = {"title"})
public class Survey implements Serializable {

    private static final long serialVersionUID = 2943648242656547434L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, length = 128)
    private String title;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate = LocalDateTime.now();

    @Enumerated
    @Column(nullable = false)
    private SurveyStatus status = SurveyStatus.NON_ACTIVE;

    private String imageUrl;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active = true;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "survey", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<SurveyQuestion> surveyQuestions = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "survey")
    private Set<SurveyContact> surveyContacts = new HashSet<>();

    public Survey(Long id) {
        this.id = id;
    }

    public void addQuestion(SurveyQuestion surveyQuestion) {
        surveyQuestions.add(surveyQuestion);
        surveyQuestion.setSurvey(this);
    }

}
