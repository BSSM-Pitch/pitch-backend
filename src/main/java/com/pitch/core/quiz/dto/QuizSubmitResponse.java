package com.pitch.core.quiz.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class QuizSubmitResponse {
    private final int score;
    private final int totalQuestions;
    private final List<QuizResultItemResponse> results;

    public QuizSubmitResponse(int score, int totalQuestions, List<QuizResultItemResponse> results) {
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.results = results;
    }
}
