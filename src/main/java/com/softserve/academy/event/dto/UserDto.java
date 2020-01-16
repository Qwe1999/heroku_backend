package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private boolean active;
    private LocalDate creationDate;
    private Roles role;

}
