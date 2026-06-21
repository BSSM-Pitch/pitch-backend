package com.pitch.core.quiz.dto;

import com.pitch.core.quiz.entity.Quiz;
import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class QuizDetailResponse {
    private final String id;
    private final List<QuizQuestionResponse> questions;

    public QuizDetailResponse(Quiz quiz) {
        this.id = "quiz_" + String.format("%03d", quiz.getId());
        this.questions = quiz.getQuestions().stream().map(QuizQuestionResponse::new).collect(Collectors.toList());
    }
}
