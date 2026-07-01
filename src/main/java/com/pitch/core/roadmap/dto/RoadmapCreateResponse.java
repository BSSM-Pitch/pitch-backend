package com.pitch.core.roadmap.dto;

import com.pitch.core.roadmap.entity.Roadmap;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RoadmapCreateResponse {
    private final UUID curriculumId;
    private final UUID roadmapId;
    private final String title;
    private final String createdAt;
    private final String updatedAt;

    public RoadmapCreateResponse(Roadmap roadmap) {
        this.curriculumId = roadmap.getCurriculum().getId();
        this.roadmapId = roadmap.getId();
        this.title = roadmap.getTitle();
        this.createdAt = roadmap.getCreatedAt().toString();
        this.updatedAt = roadmap.getUpdatedAt().toString();
    }
}
