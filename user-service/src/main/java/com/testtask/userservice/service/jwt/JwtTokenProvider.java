package com.testtask.userservice.service.jwt;

import com.testtask.userservice.config.configurer.TokenKeyConfigurer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final long EXPIRATION_TIME = 5 * 60 * 60 * 1000;

    private final Key key;

    public JwtTokenProvider(TokenKeyConfigurer tokenKeyConfigurer) {
        this.key = Keys.hmacShaKeyFor(tokenKeyConfigurer.getTokenKey().getBytes());
    }

    public String generateToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        Claims claims = Jwts.claims().setId(userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
