package com.pitch.core.quiz.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class QuizSubmitRequest {
    private List<QuizAnswerRequest> answers;
}
