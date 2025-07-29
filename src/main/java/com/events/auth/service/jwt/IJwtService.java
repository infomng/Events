package com.events.auth.service.jwt;

import com.events.user.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.function.Function;

public interface IJwtService {

  String generateToken(User user);

  String generateToken(String email);

  <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

  String extractUsername(String token);

  boolean isTokenValid(String token, UserDetails userDetails);

  boolean isTokenExpired(String token);
}
