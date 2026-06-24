package com.pitch.core.quiz.dto;

import com.pitch.core.quiz.entity.Quiz;
import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class QuizListResponse {
    private final List<QuizSummaryResponse> quizzes;

    public QuizListResponse(List<Quiz> quizzes) {
        this.quizzes = quizzes.stream().map(QuizSummaryResponse::new).collect(Collectors.toList());
    }
}
