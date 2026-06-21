package com.pitch.core.curriculum.repository;

import com.pitch.core.curriculum.entity.Curriculum;
import com.pitch.core.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {
    Optional<Curriculum> findByIdAndUser(Long id, User user);
}
