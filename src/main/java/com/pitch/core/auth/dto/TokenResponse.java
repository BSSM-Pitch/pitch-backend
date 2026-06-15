package com.pitch.core.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 토큰 재발급 응답 DTO
 */
@Builder
public record TokenResponse(

        @Schema(description = "액세스 토큰", example = "eyJhbGci...")
        String accessToken,

        @Schema(description = "리프레시 토큰", example = "eyJhbGci...")
        String refreshToken
) {
}
