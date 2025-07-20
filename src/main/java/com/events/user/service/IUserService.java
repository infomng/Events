package com.events.user.service;

import com.events.auth.dto.CreateUserCommand;
import com.events.user.dto.GetUserDto;
import com.events.user.entity.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findByEmail(String email);

    Long createUser(CreateUserCommand command);
}
