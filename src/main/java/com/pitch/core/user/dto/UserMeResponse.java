package com.pitch.core.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * 내 계정 정보 조회 응답 DTO
 */
@Builder
public record UserMeResponse(

        @Schema(description = "사용자 ID", example = "70f0b5a9-40ee-46ec-8dda-2f86a58a3dd6")
        UUID id,

        @Schema(description = "이메일", example = "student@bssm.hs.kr")
        String email,

        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "설치한 도구 목록", example = "[\"code\", \"terminal\"]")
        List<String> installedTools,

        @Schema(description = "가입 일시", example = "2026-06-04T00:00:00Z")
        Instant createdAt
) {
}
