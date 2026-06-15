package com.pitch.core.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 로그인 요청 DTO
 */
public record LoginRequest(

        @Schema(description = "이메일", example = "student@bssm.hs.kr")
        @NotBlank
        @Email
        String email,

        @Schema(description = "비밀번호", example = "abcd1234")
        @NotBlank
        String password
) {
}
