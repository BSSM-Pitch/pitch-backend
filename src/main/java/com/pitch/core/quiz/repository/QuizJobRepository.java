package com.pitch.core.quiz.repository;

import com.pitch.core.curriculum.entity.Curriculum;
import com.pitch.core.quiz.entity.QuizJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizJobRepository extends JpaRepository<QuizJob, String> {
    Optional<QuizJob> findByIdAndCurriculum(String id, Curriculum curriculum);
}
