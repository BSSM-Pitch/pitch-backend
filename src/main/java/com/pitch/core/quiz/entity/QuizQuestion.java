package com.pitch.core.quiz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quiz_questions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    @Setter
    private Quiz quiz;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT")
    private String options;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String answer;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "sort_order")
    private int sortOrder;

    public static QuizQuestion create(String type, String question, String options,
                                       String answer, String explanation, int order) {
        QuizQuestion q = new QuizQuestion();
        q.type = type;
        q.question = question;
        q.options = options;
        q.answer = answer;
        q.explanation = explanation;
        q.sortOrder = order;
        return q;
    }
}
