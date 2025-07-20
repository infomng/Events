package com.events.user.dto.mapper;

import com.events.auth.dto.CreateUserCommand;
import com.events.user.dto.GetUserDto;
import com.events.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    GetUserDto toGetUserDto(User user);

    User toEntity(CreateUserCommand command);
}
