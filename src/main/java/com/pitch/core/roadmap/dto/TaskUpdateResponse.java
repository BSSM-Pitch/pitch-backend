package com.pitch.core.roadmap.dto;

import com.pitch.core.roadmap.entity.RoadmapTask;
import lombok.Getter;

@Getter
public class TaskUpdateResponse {
    private final Long taskId;
    private final boolean completed;
    private final String updatedAt;

    public TaskUpdateResponse(RoadmapTask task) {
        this.taskId = task.getId();
        this.completed = task.isCompleted();
        this.updatedAt = task.getUpdatedAt().toString();
    }
}
