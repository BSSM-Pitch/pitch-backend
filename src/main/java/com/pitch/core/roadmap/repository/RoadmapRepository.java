package com.pitch.core.roadmap.repository;

import com.pitch.core.curriculum.entity.Curriculum;
import com.pitch.core.roadmap.entity.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    Optional<Roadmap> findByCurriculum(Curriculum curriculum);
    boolean existsByCurriculum(Curriculum curriculum);
}
