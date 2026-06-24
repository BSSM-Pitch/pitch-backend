package com.pitch.core.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatUpdateRequest(
        @NotBlank @Size(max = 100) String title
) {}
