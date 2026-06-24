package com.pitch.core.chat.dto;

import java.time.Instant;
import java.util.UUID;

public record ChatCreateResponse(
        UUID id,
        String title,
        Instant createdAt,
        Instant updatedAt,
        long messageCount,
        boolean hasCurriculum
) {}
