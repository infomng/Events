package com.events.auth.refreshtoken.service;

import com.events.auth.dto.LoginResponse;
import com.events.auth.exception.InvalidRefreshTokenEXception;
import com.events.auth.exception.UserNotFoundException;
import com.events.auth.refreshtoken.Entity.RefreshToken;
import com.events.auth.refreshtoken.repository.RefreshTokenRepository;
import com.events.auth.service.jwt.impl.JwtService;
import com.events.user.entity.User;
import com.events.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

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
                .token(UUID.randomUUID().toString())
                .build();

        this.refreshTokenRepository.save(token);

        return token;
    }

    public LoginResponse getAccessToken( String refreshToken ) {

        RefreshToken token = verifyRefreshToken(refreshToken);

        User user = token.getUser();
        return new LoginResponse( jwtService.generateToken(user));
    }

    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = this.refreshTokenRepository.findByToken(token)
                .orElseThrow(InvalidRefreshTokenEXception::new);

        if(refreshToken.isExpired()){
            refreshTokenRepository.deleteById(refreshToken.getId());
            throw new InvalidRefreshTokenEXception();
        }
        return refreshToken;
    }
}
