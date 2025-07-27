package com.events.auth.service.auth;

import com.events.auth.dto.AccessToken;
import com.events.auth.dto.LoginRequest;
import com.events.auth.dto.CreateUserCommand;

public interface IAuthService {
    AccessToken login(LoginRequest request);
    String register(CreateUserCommand request);

    String verifyEmail(String token);

    String resendVerificationEmail(String email);
}
