package com.pitch.core.roadmap.repository;

import com.pitch.core.roadmap.entity.RoadmapTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadmapTaskRepository extends JpaRepository<RoadmapTask, Long> {
}
