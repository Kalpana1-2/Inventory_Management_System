package com.userInventory.configuration;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.userInventory.enums.Role;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // ── Generate Key ──────────────────────────────
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ── Generate Token ────────────────────────────
    public String generateToken(String email,
                                Role role,
                                int userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role",   role.name())
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(
                    System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(),
                          SignatureAlgorithm.HS256)
                .compact();
    }

    // ── Extract Email from Token ──────────────────
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ── Extract Role from Token ───────────────────
    public String extractRole(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    // ── Extract UserId from Token ─────────────────
    public int extractUserId(String token) {
        return (int) Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId");
    }

    // ── Validate Token ────────────────────────────
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired!");
        } catch (JwtException e) {
            System.out.println("Invalid token!");
        }
        return false;
    }

    // ── Check if Token is Expired ─────────────────
    public boolean isTokenExpired(String token) {
        try {
            Date expiry = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiry.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
