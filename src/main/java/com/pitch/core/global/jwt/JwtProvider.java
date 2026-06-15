package com.pitch.core.global.jwt;

import com.pitch.core.global.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * JWT 액세스/리프레시 토큰을 생성하는 컴포넌트
 */
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtConfig jwtConfig;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 액세스 토큰 생성
     */
    public String createAccessToken(UUID userId, String email) {
        return createToken(userId, email, jwtConfig.getAccessTokenExpiration());
    }

    /**
     * 리프레시 토큰 생성
     */
    public String createRefreshToken(UUID userId, String email) {
        return createToken(userId, email, jwtConfig.getRefreshTokenExpiration());
    }

    private String createToken(UUID userId, String email, long expirationMillis) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    /**
     * 토큰을 검증하고 클레임을 추출
     * 서명 위조/형식 오류, 만료 시 jjwt 예외를 그대로 던진다 (호출부에서 처리)
     */
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 클레임에서 사용자 ID(subject)를 추출
     */
    public UUID getUserId(Claims claims) {
        return UUID.fromString(claims.getSubject());
    }

    /**
     * 클레임에서 이메일을 추출
     */
    public String getEmail(Claims claims) {
        return claims.get("email", String.class);
    }
}
