package com.softserve.academy.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "survey_contacts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"survey_id", "contact_id"}))
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SurveyContact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Survey survey;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Contact contact;

    @Column(name = "can_pass")
    private boolean canPass;

}
