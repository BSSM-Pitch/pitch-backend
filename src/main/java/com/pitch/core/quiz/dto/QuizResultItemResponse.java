package com.pitch.core.quiz.dto;

import lombok.Getter;

@Getter
public class QuizResultItemResponse {
    private final Long questionId;
    private final boolean correct;
    private final String correctAnswer;
    private final String explanation;

    public QuizResultItemResponse(Long questionId, boolean correct, String correctAnswer, String explanation) {
        this.questionId = questionId;
        this.correct = correct;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }
}
