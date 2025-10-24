package com.events.modules.auth.refreshtoken.service;

import com.events.modules.auth.dto.AccessToken;
import com.events.modules.auth.exception.InvalidRefreshTokenException;
import com.events.modules.auth.exception.UserNotFoundException;
import com.events.modules.auth.refreshtoken.Entity.RefreshToken;
import com.events.modules.auth.refreshtoken.repository.RefreshTokenRepository;
import com.events.modules.auth.service.jwt.impl.JwtService;
import com.events.modules.user.entity.User;
import com.events.modules.user.repository.IUserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final IUserRepository userRepository;
    private final JwtService jwtService;

    @Value("${app.security.refresh-token.expiration}") // Ex: 7 jours
    private Long refreshTokenDurationMs;

    public RefreshToken createRefreshToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        RefreshToken token = RefreshToken.builder()
                .expirationDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .user(user)
                .token(jwtService.generateToken(user))
                .build();

        this.refreshTokenRepository.save(token);

        return token;
    }

    public AccessToken getAccessToken(HttpServletRequest request ) {
        Cookie[] cookies = request.getCookies();
       if (cookies == null) {
           throw new InvalidRefreshTokenException();
       }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        RefreshToken token = verifyRefreshToken(refreshToken);

        User user = token.getUser();
        return new AccessToken( jwtService.generateToken(user));
    }

    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = this.refreshTokenRepository.findByToken(token)
                .orElseThrow(InvalidRefreshTokenException::new);

        if(refreshToken.isExpired()){
            refreshTokenRepository.deleteById(refreshToken.getId());
            throw new InvalidRefreshTokenException();
        }
        return refreshToken;
    }
}
