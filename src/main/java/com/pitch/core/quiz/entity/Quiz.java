package com.pitch.core.quiz.entity;

import com.pitch.core.curriculum.entity.Curriculum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_id", nullable = false)
    private Curriculum curriculum;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    @OrderBy("sortOrder ASC")
    private List<QuizQuestion> questions = new ArrayList<>();

    private Integer score;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime submittedAt;

    public static Quiz create(Curriculum curriculum) {
        Quiz q = new Quiz();
        q.curriculum = curriculum;
        return q;
    }

    public void addQuestion(QuizQuestion question) {
        questions.add(question);
        question.setQuiz(this);
    }

    public void submit(int score) {
        this.score = score;
        this.submittedAt = LocalDateTime.now();
    }
}
