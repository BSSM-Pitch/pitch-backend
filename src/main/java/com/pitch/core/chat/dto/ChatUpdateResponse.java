package com.pitch.core.chat.dto;

import java.time.Instant;
import java.util.UUID;

public record ChatUpdateResponse(UUID id, String title, Instant updatedAt) {}
