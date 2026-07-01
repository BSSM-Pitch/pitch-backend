package com.pitch.core.roadmap.dto;

import com.pitch.core.roadmap.entity.RoadmapTask;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RoadmapTaskResponse {
    private final UUID id;
    private final String title;
    private final String description;
    private final String startDate;
    private final String endDate;
    private final String startTime;
    private final String endTime;
    private final boolean completed;
    private final int order;

    public RoadmapTaskResponse(RoadmapTask task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.startDate = task.getStartDate() != null ? task.getStartDate().toString() : null;
        this.endDate = task.getEndDate() != null ? task.getEndDate().toString() : null;
        this.startTime = task.getStartTime();
        this.endTime = task.getEndTime();
        this.completed = task.isCompleted();
        this.order = task.getSortOrder();
    }
}
