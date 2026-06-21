package com.pitch.core.quiz.entity;

import com.pitch.core.curriculum.entity.Curriculum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "quiz_jobs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizJob {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_id", nullable = false)
    private Curriculum curriculum;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private int estimatedSeconds;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum JobStatus { PENDING, COMPLETED, FAILED }

    public static QuizJob create(Curriculum curriculum, int estimatedSeconds) {
        QuizJob job = new QuizJob();
        job.id = "quiz_job_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        job.curriculum = curriculum;
        job.status = JobStatus.PENDING;
        job.estimatedSeconds = estimatedSeconds;
        return job;
    }

    public void markCompleted() { this.status = JobStatus.COMPLETED; }
    public void markFailed() { this.status = JobStatus.FAILED; }
}
