package com.pitch.core.chat.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChatListResponse(List<ChatItem> chats, Pagination pagination) {

    public record ChatItem(
            UUID id,
            String title,
            Instant createdAt,
            Instant updatedAt,
            long messageCount,
            boolean hasCurriculum
    ) {}

    public record Pagination(int page, int limit, long total) {}
}
