package com.events.modules.auth.service.auth;

import com.events.modules.auth.dto.AccessToken;
import com.events.modules.auth.dto.LoginRequest;
import com.events.modules.auth.dto.RegisterCommand;
import com.events.modules.auth.dto.ForgotPasswordRequest;
import com.events.modules.auth.dto.ResetPasswordRequest;
import com.events.modules.user.entity.User;

public interface IAuthService {
    AccessToken login(LoginRequest request);

    String register(RegisterCommand request);

    String verifyEmail(String token);

    String resendVerificationEmail(String email);

    String forgotPassword(ForgotPasswordRequest request);

    String resetPassword(ResetPasswordRequest request);

    User getCurrentUser();
}
