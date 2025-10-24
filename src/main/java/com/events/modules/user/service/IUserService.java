package com.events.modules.user.service;

import com.events.modules.auth.dto.RegisterCommand;
import com.events.modules.user.entity.User;

import java.util.Optional;

public interface IUserService {
    void createUser(RegisterCommand command);

    User findByEmail(String email);

    boolean existsByEmail(String email);
}
