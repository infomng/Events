package com.events.auth.service.auth;

import com.events.auth.dto.LoginRequest;
import com.events.auth.dto.LoginResponse;
import com.events.auth.dto.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IAuthService {
    LoginResponse login(LoginRequest request);
    String register(RegisterRequest request);

    String verifyEmail(String token);
}
