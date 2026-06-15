package com.pitch.core.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 회원가입/로그인 공통 인증 응답 DTO
 */
@Builder
public record AuthResponse<T>(

        @Schema(description = "액세스 토큰", example = "eyJhbGci...")
        String accessToken,

        @Schema(description = "리프레시 토큰", example = "eyJhbGci...")
        String refreshToken,

        @Schema(description = "사용자 정보")
        T user
) {
}
