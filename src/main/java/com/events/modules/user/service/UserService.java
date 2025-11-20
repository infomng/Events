package com.events.modules.user.service;

import com.events.modules.auth.dto.RegisterCommandDto;
import com.events.modules.auth.exception.UserNotFoundException;
import com.events.modules.user.entity.User;
import com.events.modules.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Primary
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository userRepository;

    @Override
    public void createUser(RegisterCommandDto command) {
        User user = new User();
        user.setFullName(command.fullName());
        user.setEmail(command.email());
        user.setPassword(command.password());
        user.setRole(command.role());
        user.setVerificationToken(command.verificationToken());
        user.setVerified(false);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existByEmail(email);
    }
}
