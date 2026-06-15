package com.pitch.core.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 토큰 재발급 요청 DTO
 */
public record RefreshRequest(

        @Schema(description = "리프레시 토큰", example = "eyJhbGci...")
        @NotBlank
        String refreshToken
) {
}
