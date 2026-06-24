package com.pitch.core.roadmap.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class RoadmapPartRequest {
    private String title;
    private int order;
    private List<RoadmapTaskRequest> tasks;
}
