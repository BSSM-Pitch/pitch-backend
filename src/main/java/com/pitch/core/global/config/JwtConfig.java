package com.pitch.core.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * application.yml의 jwt.* 프로퍼티를 바인딩하는 설정 클래스
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /** JWT 서명에 사용되는 비밀키 */
    private String secret;

    /** 액세스 토큰 만료 시간 (ms) */
    private long accessTokenExpiration;

    /** 리프레시 토큰 만료 시간 (ms) */
    private long refreshTokenExpiration;
}
