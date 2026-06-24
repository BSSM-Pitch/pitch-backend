package com.pitch.core.roadmap.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import java.util.List;

@Getter
public class RoadmapUpdateRequest {
    @NotBlank private String title;
    private List<RoadmapPartRequest> parts;
}
