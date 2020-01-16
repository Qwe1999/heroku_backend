package com.softserve.academy.event.util;

import com.softserve.academy.event.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
public class EmailAddress {

    @Pattern(regexp = User.EMAIL_PATTERN)
    String email;

}
