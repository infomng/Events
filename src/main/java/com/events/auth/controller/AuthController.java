package com.events.auth.controller;

import com.events.auth.dto.AccessToken;
import com.events.auth.dto.LoginRequest;
import com.events.auth.dto.LoginResponse;
import com.events.auth.dto.CreateUserCommand;
import com.events.auth.refreshtoken.Entity.RefreshToken;
import com.events.auth.refreshtoken.service.RefreshTokenService;
import com.events.auth.service.auth.AuthService;
import com.events.common.result.Result;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CreateUserCommand request){
        return ResponseEntity.ok(authService.register(request));
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
    public ResponseEntity<LoginResponse> refresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return ResponseEntity.status(401).build();

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        return  ResponseEntity.ok(refreshTokenService.getAccessToken(refreshToken));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Result<String>> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(Result.success(authService.verifyEmail(token)));
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<Result<String>> resendVerificationEmail(@RequestParam String email) {
        return ResponseEntity.ok(Result.success(authService.resendVerificationEmail(email)));
    }
}
