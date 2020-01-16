package com.softserve.academy.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@NamedQuery(
        name = "findEmailContact",
        query = "from Contact u where u.email= :email"
)

@Entity
@Table(name = "contacts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email", "user_id"}))
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contact implements Serializable {

    private static final long serialVersionUID = 6144143087405979275L;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(length = 128)
    private String name;

    @Pattern(regexp = EMAIL_PATTERN)
    @Column(length = 128, nullable = false)
    private String email;

    @OneToMany(mappedBy = "contact")
    private Set<SurveyContact> surveyContacts = new HashSet<>();

}
