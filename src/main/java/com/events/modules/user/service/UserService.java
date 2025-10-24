package com.events.modules.user.service;

import com.events.modules.auth.dto.RegisterCommand;
import com.events.modules.auth.exception.UserNotFoundException;
import com.events.modules.user.entity.User;
import com.events.modules.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository userRepository;

    @Override
    public void createUser(RegisterCommand command) {
        User user = User.builder()
                .fullName(command.fullName())
                .email(command.email())
                .password(command.password())
                .role(command.role())
                .verificationToken(command.verificationToken())
                .isVerified(false)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .build();

        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existByEmail(email);
    }
}
