package com.pitch.core.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 회원가입 요청 DTO
 */
public record SignupRequest(

        @Schema(description = "이메일", example = "student@bssm.hs.kr")
        @NotBlank
        @Email
        String email,

        @Schema(description = "비밀번호 (8자 이상, 영문+숫자 조합)", example = "abcd1234")
        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$")
        String password,

        @Schema(description = "이름", example = "홍길동")
        @NotBlank
        String name
) {
}
