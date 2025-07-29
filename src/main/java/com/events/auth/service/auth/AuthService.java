package com.events.auth.service.auth;

import com.events.auth.dto.AccessToken;
import com.events.auth.dto.LoginRequest;
import com.events.auth.dto.CreateUserCommand;
import com.events.auth.dto.ForgotPasswordRequest;
import com.events.auth.dto.ResetPasswordRequest;
import com.events.auth.enumeration.RoleEnum;
import com.events.auth.exception.EmailAlreadyExistException;
import com.events.auth.exception.UserNotFoundException;
import com.events.auth.service.email.IEmailService;
import com.events.auth.service.jwt.IJwtService;
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
@Transactional(propagation = Propagation.REQUIRED)
public class AuthService implements IAuthService {
    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    private final IEmailService emailService;

    @Override
    public String register(CreateUserCommand request) {


        try {
            if (userService.findByEmail(request.email()).isPresent()) {
                throw new EmailAlreadyExistException(request.email());
            }

            String token = jwtService.generateToken(request.email());

            CreateUserCommand command = CreateUserCommand.builder()
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .fullName(request.fullName())
                    .verificationToken(token)
                    .role(RoleEnum.USER)
                    .build();

            userService.createUser(command);

            emailService.sendVerificationEmail(request.email(), token);

            return "Verification email sent to " + request.email() + ". Please check your inbox.";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String verifyEmail(String token) {
        if(token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        String email = jwtService.extractUsername(token);

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if(user.isVerified()){
            throw  new IllegalStateException("User already verified");
        }

        if(user.getVerificationToken() == null || !user.getVerificationToken().equals(token)) {
            throw new IllegalArgumentException("Invalid verification token");
        }

        user.setVerified(true);
        user.setVerificationToken(null);


        return "User " + user.getFullName() + " with email " + user.getEmail() + " has been successfully verified.";
    }

    @Override
    public String resendVerificationEmail(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (user.isVerified()) {
            throw new IllegalStateException("User already verified");
        }

        String token = jwtService.generateToken(email);
        user.setVerificationToken(token);

        emailService.sendVerificationEmail(email, token);

        return "Verification email resent to " + email + ". Please check your inbox.";
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

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {
        User user = userService.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException(request.email()));

        String token = jwtService.generateToken(user.getEmail());
        user.setResetPasswordToken(token);
        emailService.sendResetPasswordEmail(user.getEmail(), token);
        return "Password reset email sent to " + user.getEmail() + ". Please check your inbox.";
    }

    @Override
    public String resetPassword(ResetPasswordRequest request) {
        String email = jwtService.extractUsername(request.token());
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        if(user.getResetPasswordToken() == null || !user.getResetPasswordToken().equals(request.token())) {
            throw new IllegalArgumentException("Invalid or expired token");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setResetPasswordToken(null);

        return "Password has been reset successfully for " + user.getEmail();
    }
}
