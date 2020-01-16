package com.softserve.academy.event.entity;

import com.softserve.academy.event.entity.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@NamedQuery(
        name = "findEmail",
        query = "from User u where u.email= :email"
)
@NamedQuery(
        name = "findEmailById",
        query = "from User u where u.id= :id"
)
@Entity
@Table(name = "users")
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 8894016998310477567L;
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9]+(\\.[_A-Za-z0-9]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(regexp = EMAIL_PATTERN)
    @Column(unique = true, length = 128)
    private String email;

    @Column(length = 100)
    @Nullable
    private String password;

    private boolean active;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate creationDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private Roles role = Roles.USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Survey> surveys = new HashSet<>();

    public User() {
        this.active = false;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.active = false;
    }


    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return email;
    }

    public Roles getRole() {
        return role;
    }

}
