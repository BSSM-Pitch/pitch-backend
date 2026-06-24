package com.pitch.core.roadmap.dto;

import com.pitch.core.roadmap.entity.RoadmapPart;
import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RoadmapPartResponse {
    private final Long id;
    private final String title;
    private final int order;
    private final List<RoadmapTaskResponse> tasks;

    public RoadmapPartResponse(RoadmapPart part) {
        this.id = part.getId();
        this.title = part.getTitle();
        this.order = part.getSortOrder();
        this.tasks = part.getTasks().stream().map(RoadmapTaskResponse::new).collect(Collectors.toList());
    }
}
