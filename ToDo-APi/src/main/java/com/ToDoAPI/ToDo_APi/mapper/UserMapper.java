package com.ToDoAPI.ToDo_APi.mapper;

import com.ToDoAPI.ToDo_APi.DTO.UserDto;
import com.ToDoAPI.ToDo_APi.DTO.UserLoginResponseDto;
import com.ToDoAPI.ToDo_APi.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract User toUser(UserDto userDto);

    public abstract UserLoginResponseDto toUserLogin(User user);
}
