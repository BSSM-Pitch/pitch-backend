package com.pitch.core.quiz.dto;

import com.pitch.core.quiz.entity.Quiz;
import lombok.Getter;

@Getter
public class QuizSummaryResponse {
    private final String id;
    private final int questionCount;
    private final String createdAt;
    private final String submittedAt;
    private final Integer score;

    public QuizSummaryResponse(Quiz quiz) {
        this.id = "quiz_" + String.format("%03d", quiz.getId());
        this.questionCount = quiz.getQuestions().size();
        this.createdAt = quiz.getCreatedAt().toString();
        this.submittedAt = quiz.getSubmittedAt() != null ? quiz.getSubmittedAt().toString() : null;
        this.score = quiz.getScore();
    }
}
