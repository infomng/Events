package com.events.auth.service.auth;

import com.events.auth.dto.AccessToken;
import com.events.auth.dto.LoginRequest;
import com.events.auth.dto.CreateUserCommand;
import com.events.auth.dto.ForgotPasswordRequest;
import com.events.auth.dto.ResetPasswordRequest;

public interface IAuthService {
    AccessToken login(LoginRequest request);

    String register(CreateUserCommand request);

    String verifyEmail(String token);

    String resendVerificationEmail(String email);

    String forgotPassword(ForgotPasswordRequest request);

    String resetPassword(ResetPasswordRequest request);
}
