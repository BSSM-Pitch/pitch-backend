package com.pitch.core.quiz.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class QuizResultItemResponse {
    private final UUID questionId;
    private final boolean correct;
    private final String correctAnswer;
    private final String explanation;

    public QuizResultItemResponse(UUID questionId, boolean correct, String correctAnswer, String explanation) {
        this.questionId = questionId;
        this.correct = correct;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }
}
