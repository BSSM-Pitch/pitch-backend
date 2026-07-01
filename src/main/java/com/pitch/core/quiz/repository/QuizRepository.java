package com.pitch.core.quiz.repository;

import com.pitch.core.curriculum.entity.Curriculum;
import com.pitch.core.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCurriculumOrderByCreatedAtDesc(Curriculum curriculum);
    Optional<Quiz> findByIdAndCurriculum(UUID id, Curriculum curriculum);
}
