package com.pitch.core.chat.dto;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPQL 생성자 표현식 전용 DTO.
 * COUNT()는 Long을 반환하므로 생성자에서 boolean으로 변환한다.
 */
@Getter
public class ChatSummaryDto {

    private final UUID id;
    private final String title;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final long messageCount;
    private final boolean hasCurriculum;

    public ChatSummaryDto(UUID id, String title, Instant createdAt, Instant updatedAt,
                          Long messageCount, Long curriculumCount) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.messageCount = messageCount != null ? messageCount : 0L;
        this.hasCurriculum = curriculumCount != null && curriculumCount > 0L;
    }
}
