package com.pitch.core.quiz.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import java.util.List;

@Getter
public class QuizGenerateRequest {
    @Min(1) @Max(20)
    private int count;
    @NotEmpty
    private List<String> types;
}
