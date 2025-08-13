package com.kss.backend.config;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kss.backend.user.User;
import com.kss.backend.user.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * JwtService
 */
@Service
public class JwtService {

    @Value("${jwt.secret:85904kbhXjteswsghjksew}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    private final UserService userService;

    public JwtService(UserService userService) {
        this.userService = userService;
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmailFromToken(String token) {
        try {
            return getClaims(token).getSubject();
        } catch (JwtException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public String getRoleFromToken(String token) {
        try {
            return (String) getClaims(token).get("role");
        } catch (JwtException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public boolean verifyUser(String token) {
        String email = getEmailFromToken(token);
        String role = getRoleFromToken(token);

        User user = userService.findByEmail(email);
        return user.getRole().toString().equals(role);
    }

}
