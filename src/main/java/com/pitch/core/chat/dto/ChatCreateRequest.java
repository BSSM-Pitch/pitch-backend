package com.pitch.core.chat.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record ChatCreateRequest(
        @NotBlank String title,
        List<UUID> fileIds
) {}
