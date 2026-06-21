package com.pitch.core.roadmap.dto;

import com.pitch.core.roadmap.entity.Roadmap;
import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RoadmapResponse {
    private final Long curriculumId;
    private final Long roadmapId;
    private final String title;
    private final String createdAt;
    private final String updatedAt;
    private final List<RoadmapPartResponse> parts;

    public RoadmapResponse(Roadmap roadmap) {
        this.curriculumId = roadmap.getCurriculum().getId();
        this.roadmapId = roadmap.getId();
        this.title = roadmap.getTitle();
        this.createdAt = roadmap.getCreatedAt().toString();
        this.updatedAt = roadmap.getUpdatedAt().toString();
        this.parts = roadmap.getParts().stream().map(RoadmapPartResponse::new).collect(Collectors.toList());
    }
}
