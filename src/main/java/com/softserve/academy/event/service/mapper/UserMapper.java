package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.UserDto;
import com.softserve.academy.event.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
@Service
public interface UserMapper {
    User userDtoToUser(UserDto userDto);
    UserDto userToDto(User user);
}
