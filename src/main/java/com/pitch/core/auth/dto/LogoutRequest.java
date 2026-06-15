package com.pitch.core.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 로그아웃 요청 DTO
 */
public record LogoutRequest(

        @Schema(description = "리프레시 토큰", example = "eyJhbGci...")
        @NotBlank
        String refreshToken
) {
}
