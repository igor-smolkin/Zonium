package org.ataraxii.zoniumauth.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.ataraxii.zoniumauth.database.entity.RoleType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private static final long ACCESS_TOKEN_VALIDITY_MS = 60 * 60 * 1000;
    private static final long REFRESH_TOKEN_VALIDITY_MS = 7 * 24 * 60 * 60 * 1000;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && validateToken(token);
    }

    public String generateAccessToken(String username, UUID userId, List<RoleType> roles) {
        List<String> roleNames = roles.stream()
                .map(Enum::name)
                .toList();

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("roles", roleNames)
                .issuedAt(new Date())
                .expiration(getAccessTokenExpiryDate())
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(getRefreshTokenExpiryDate())
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Date getRefreshTokenExpiryDate() {
        return new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_MS); // 30 дней
    }

    public Date getAccessTokenExpiryDate() {
        return new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_MS); // 48 часов
    }
}