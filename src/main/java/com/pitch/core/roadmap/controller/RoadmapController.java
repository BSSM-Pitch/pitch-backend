package com.pitch.core.roadmap.controller;

import com.pitch.core.roadmap.dto.*;
import com.pitch.core.roadmap.service.RoadmapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/curricula/{curriculumId}/roadmap")
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapService roadmapService;

    @GetMapping
    public ResponseEntity<RoadmapResponse> getRoadmap(
            @PathVariable UUID curriculumId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(roadmapService.getRoadmap(curriculumId, userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<RoadmapCreateResponse> createRoadmap(
            @PathVariable UUID curriculumId,
            @RequestBody @Valid RoadmapCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roadmapService.createRoadmap(curriculumId, request, userDetails.getUsername()));
    }

    @PatchMapping
    public ResponseEntity<RoadmapCreateResponse> updateRoadmap(
            @PathVariable UUID curriculumId,
            @RequestBody @Valid RoadmapUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(roadmapService.updateRoadmap(curriculumId, request, userDetails.getUsername()));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteRoadmap(
            @PathVariable UUID curriculumId,
            @AuthenticationPrincipal UserDetails userDetails) {
        roadmapService.deleteRoadmap(curriculumId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity<TaskUpdateResponse> updateTaskCompletion(
            @PathVariable UUID curriculumId,
            @PathVariable Long taskId,
            @RequestBody TaskUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(roadmapService.updateTaskCompletion(
                curriculumId, taskId, request, userDetails.getUsername()));
    }
}
