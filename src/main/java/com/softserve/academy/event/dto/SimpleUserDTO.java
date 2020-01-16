package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.Roles;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SimpleUserDTO {

    private Long id;
    private String email;
    private String password;
    private boolean active;
    private LocalDate creationDate;
    private Roles role;

    private SimpleUserDTO() {
    }

    public static SimpleUserDTO toSimpleUser(User user) {
        SimpleUserDTO dto = new SimpleUserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setActive(user.isActive());
        dto.setCreationDate(user.getCreationDate());
        dto.setRole(user.getRole());
        return dto;
    }

}
