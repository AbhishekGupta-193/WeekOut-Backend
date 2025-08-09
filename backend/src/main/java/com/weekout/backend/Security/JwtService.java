package com.weekout.backend.Security;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String base64Secret;
    private final long expirationMs;

    public JwtService() {
        Dotenv dotenv = Dotenv.load();
        this.base64Secret = dotenv.get("JWT_SECRET");
        this.expirationMs = Long.parseLong(dotenv.get("JWT_EXPIRATION_MS", "3600000")); // default 1 hour if not set
    }

    private Key getSigningKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(base64Secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String subject) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
