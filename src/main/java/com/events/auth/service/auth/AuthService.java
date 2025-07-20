package com.events.auth.service.auth;

import com.events.auth.dto.AccessToken;
import com.events.auth.dto.LoginRequest;
import com.events.auth.dto.CreateUserCommand;
import com.events.auth.exception.EmailAlreadyExistException;
import com.events.auth.exception.UserNotFoundException;
import com.events.auth.service.email.IEmailService;
import com.events.auth.service.jwt.JwtService;
import com.events.user.entity.User;

import com.events.user.service.IUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    private final IEmailService emailService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String register(CreateUserCommand request) {

        if(userService.findByEmail(request.email()).isPresent()){
            throw new EmailAlreadyExistException(request.email());
        }

        String token = jwtService.generateToken(request.email());

        CreateUserCommand command = CreateUserCommand.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .verificationToken(token)
                .build();

        emailService.sendVerificationEmail(request.email(), token);

        userService.createUser(command);

        return "Verification email sent to " + request.email() + ". Please check your inbox.";
    }

    @Override
    public String verifyEmail(String token) {
        return "";
    }

    public AccessToken login(LoginRequest request) {
        authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userService.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException(request.email()));

        String jwt = jwtService.generateToken(user);

        return AccessToken.builder().access_token(jwt).build();
    }
}
