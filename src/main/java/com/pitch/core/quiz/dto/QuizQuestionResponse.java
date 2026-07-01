package com.pitch.core.quiz.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitch.core.quiz.entity.QuizQuestion;
import lombok.Getter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class QuizQuestionResponse {
    private final UUID id;
    private final String type;
    private final String question;
    private final List<String> options;
    private final String answer;
    private final String explanation;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public QuizQuestionResponse(QuizQuestion q) {
        this.id = q.getId();
        this.type = q.getType();
        this.question = q.getQuestion();
        this.options = parseOptions(q.getOptions());
        this.answer = q.getAnswer();
        this.explanation = q.getExplanation();
    }

    private static List<String> parseOptions(String json) {
        if (json == null) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
