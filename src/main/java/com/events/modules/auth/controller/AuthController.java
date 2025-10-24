package com.events.modules.auth.controller;

import com.events.modules.auth.dto.AccessToken;
import com.events.modules.auth.dto.LoginRequest;
import com.events.modules.auth.dto.RegisterCommand;
import com.events.modules.auth.dto.ForgotPasswordRequest;
import com.events.modules.auth.dto.ResetPasswordRequest;
import com.events.modules.auth.refreshtoken.Entity.RefreshToken;
import com.events.modules.auth.refreshtoken.service.RefreshTokenService;
import com.events.modules.auth.service.auth.IAuthService;
import com.events.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();

    @PostMapping("/register")
    public ResponseEntity<Result<String>> register(@RequestBody RegisterCommand request){
        return ResponseEntity.ok(Result.success(authService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<Result<AccessToken>> login(HttpServletRequest request) {
        final var token = authenticationConverter.convert(request);
        final var accessToken = authService.login(new LoginRequest(
                token.getName(),
                token.getCredentials().toString()
        ));

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(token.getName());

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(Result.success(accessToken));
    }

    @PostMapping("refresh-token")
    public ResponseEntity<AccessToken> refresh(HttpServletRequest request) {
        return  ResponseEntity.ok(refreshTokenService.getAccessToken(request));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Result<String>> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(Result.success(authService.verifyEmail(token)));
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<Result<String>> resendVerificationEmail(@RequestParam String email) {
        return ResponseEntity.ok(Result.success(authService.resendVerificationEmail(email)));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Result<String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(Result.success(authService.forgotPassword(request)));
    }

    @GetMapping("/reset-password")
    public ResponseEntity<Result<String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(Result.success(authService.resetPassword(request)));
    }
}
