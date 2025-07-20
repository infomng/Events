package com.events.user.service;

import com.events.auth.dto.CreateUserCommand;
import com.events.user.dto.GetUserDto;
import com.events.user.dto.mapper.IUserMapper;
import com.events.user.entity.User;
import com.events.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final IUserMapper userMapper;

    @Override
    public Long createUser(CreateUserCommand command) {
        User user = userMapper.toEntity(command);
        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
