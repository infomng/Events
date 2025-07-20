package com.events.user.service;

import com.events.user.dto.GetUserDto;
import com.events.user.entity.User;

import java.util.Optional;

public interface IUserService {
    GetUserDto findByEmail(String email);
    Optional<User> findByUsername(String email);
}
