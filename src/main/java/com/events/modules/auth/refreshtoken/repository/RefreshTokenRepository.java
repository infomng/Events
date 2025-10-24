package com.events.modules.auth.refreshtoken.repository;

import com.events.modules.auth.refreshtoken.Entity.RefreshToken;
import com.events.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}