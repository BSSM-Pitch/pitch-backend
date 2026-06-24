package com.pitch.core.roadmap.dto;

import lombok.Getter;

@Getter
public class RoadmapTaskRequest {
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private int order;
}
