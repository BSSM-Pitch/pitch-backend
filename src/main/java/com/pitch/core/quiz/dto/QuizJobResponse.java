package com.pitch.core.quiz.dto;

import com.pitch.core.quiz.entity.QuizJob;
import lombok.Getter;

@Getter
public class QuizJobResponse {
    private final String jobId;
    private final String status;
    private final int estimatedSeconds;

    public QuizJobResponse(QuizJob job) {
        this.jobId = job.getId();
        this.status = job.getStatus().name().toLowerCase();
        this.estimatedSeconds = job.getEstimatedSeconds();
    }
}
