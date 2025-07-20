package com.events.auth.utils;

import com.events.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtils {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;



    private SecretKey getSecretkey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
