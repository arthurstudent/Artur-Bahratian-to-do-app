package com.testtask.taskservice.config.security.jwt;

import com.testtask.taskservice.config.configurer.TokenKeyConfigurer;
import com.testtask.taskservice.exceptions.custom.JwtValidationFailedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Log4j2
@Component
public class JwtTokenValidator {

    private final Key key;

    public JwtTokenValidator(TokenKeyConfigurer tokenKeyConfigurer) {
        this.key = Keys.hmacShaKeyFor(tokenKeyConfigurer.getTokenKey().getBytes());
    }

    public boolean validateToken(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.after(new Date());
    }

    public String getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.getId();
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token", e);
            throw new JwtValidationFailedException("JWT token is invalid");
        }
    }
}
