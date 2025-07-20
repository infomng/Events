package com.events.auth.refreshtoken.repository;

import com.events.auth.refreshtoken.Entity.RefreshToken;
import com.events.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}