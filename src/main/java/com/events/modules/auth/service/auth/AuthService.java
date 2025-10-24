package com.events.modules.auth.service.auth;

import com.events.common.utils.contants.NameOf;
import com.events.modules.auth.dto.AccessToken;
import com.events.modules.auth.dto.LoginRequest;
import com.events.modules.auth.dto.RegisterCommand;
import com.events.modules.auth.dto.ForgotPasswordRequest;
import com.events.modules.auth.dto.ResetPasswordRequest;
import com.events.modules.user.enumeration.RoleEnum;
import com.events.modules.auth.exception.EmailAlreadyExistException;
import com.events.modules.auth.exception.RegisterException;
import com.events.modules.auth.exception.UserNotFoundException;
import com.events.modules.auth.service.email.IEmailService;
import com.events.modules.auth.service.jwt.IJwtService;
import com.events.common.exception.BadRequestException;
import com.events.modules.user.entity.User;

import com.events.modules.user.service.IUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String register(RegisterCommand request) {

        try {
            if (userService.existsByEmail(request.email())) {
                throw new EmailAlreadyExistException(request.email());
            }

            String token = jwtService.generateToken(request.email());

            RegisterCommand command = RegisterCommand.builder()
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .fullName(request.fullName())
                    .verificationToken(token)
                    .role(RoleEnum.USER)
                    .build();

            userService.createUser(command);

            emailService.sendVerificationEmail(request.email(), token);

            return NameOf.VERIFICATION_EMAIL_SENT_TO + request.email() + NameOf.PLEASE_CHECK_YOUR_INBOX;
        } catch (Exception e) {
            throw new RegisterException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public String verifyEmail(String token) {
        if(token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        String email = jwtService.extractUsername(token);

        User user = userService.findByEmail(email) ;

        if(user.isVerified()){
            throw  new BadRequestException("User already verified");
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
        User user = userService.findByEmail(email);

        if (user.isVerified()) {
            throw new BadRequestException("User already verified");
        }

        String token = jwtService.generateToken(email);
        user.setVerificationToken(token);

        emailService.sendVerificationEmail(email, token);

        return "Verification email resent to " + email + NameOf.PLEASE_CHECK_YOUR_INBOX;
    }

    public AccessToken login(LoginRequest request) {
        authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userService.findByEmail(request.email());

        String jwt = jwtService.generateToken(user);

        return AccessToken.builder().access_token(jwt).build();
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {
        User user = userService.findByEmail(request.email());

        String token = jwtService.generateToken(user.getEmail());
        user.setResetPasswordToken(token);
        emailService.sendResetPasswordEmail(user.getEmail(), token);
        return "Password reset email sent to " + user.getEmail() + NameOf.PLEASE_CHECK_YOUR_INBOX;
    }

    @Override
    public String resetPassword(ResetPasswordRequest request) {
        String email = jwtService.extractUsername(request.token());
        User user = userService.findByEmail(email);
        if(user.getResetPasswordToken() == null || !user.getResetPasswordToken().equals(request.token())) {
            throw new IllegalArgumentException("Invalid or expired token");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setResetPasswordToken(null);

        return "Password has been reset successfully for " + user.getEmail();
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UserNotFoundException("Utilisateur non authentifié");
        }

        String email = authentication.getName();

        return userService.findByEmail(email);
    }
}
