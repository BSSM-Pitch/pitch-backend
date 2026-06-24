package com.pitch.core.quiz.dto;

import lombok.Getter;

@Getter
public class QuizAnswerRequest {
    private Long questionId;
    private String answer;
}
