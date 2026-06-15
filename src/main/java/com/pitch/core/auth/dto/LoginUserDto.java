package com.pitch.core.auth.dto;

import com.pitch.core.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

/**
 * 로그인 응답에 포함되는 사용자 정보 DTO
 */
@Builder
public record LoginUserDto(

        @Schema(description = "사용자 ID", example = "70f0b5a9-40ee-46ec-8dda-2f86a58a3dd6")
        UUID id,

        @Schema(description = "이메일", example = "student@bssm.hs.kr")
        String email,

        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "마지막 활동 일시", example = "2026-06-04T00:00:00Z")
        Instant lastActiveAt
) {

    public static LoginUserDto from(User user) {
        return LoginUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .lastActiveAt(user.getLastActiveAt())
                .build();
    }
}
