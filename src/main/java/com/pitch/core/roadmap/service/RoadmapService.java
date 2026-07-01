package com.pitch.core.roadmap.service;

import com.pitch.core.curriculum.entity.Curriculum;
import com.pitch.core.curriculum.repository.CurriculumRepository;
import com.pitch.core.global.exception.BusinessException;
import com.pitch.core.global.exception.ErrorCode;
import com.pitch.core.roadmap.dto.*;
import com.pitch.core.roadmap.entity.Roadmap;
import com.pitch.core.roadmap.entity.RoadmapPart;
import com.pitch.core.roadmap.entity.RoadmapTask;
import com.pitch.core.roadmap.repository.RoadmapRepository;
import com.pitch.core.roadmap.repository.RoadmapTaskRepository;
import com.pitch.core.user.entity.User;
import com.pitch.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoadmapService {

    private final CurriculumRepository curriculumRepository;
    private final RoadmapRepository roadmapRepository;
    private final RoadmapTaskRepository roadmapTaskRepository;
    private final UserRepository userRepository;

    public RoadmapResponse getRoadmap(UUID curriculumId, String email) {
        Curriculum curriculum = getCurriculum(curriculumId, email);
        Roadmap roadmap = roadmapRepository.findByCurriculum(curriculum)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROADMAP_NOT_FOUND));
        return new RoadmapResponse(roadmap);
    }

    @Transactional
    public RoadmapCreateResponse createRoadmap(UUID curriculumId, RoadmapCreateRequest request, String email) {
        Curriculum curriculum = getCurriculum(curriculumId, email);
        if (roadmapRepository.existsByCurriculum(curriculum)) {
            throw new BusinessException(ErrorCode.ROADMAP_ALREADY_EXISTS);
        }
        Roadmap roadmap = Roadmap.create(curriculum, request.getTitle());
        if (request.getParts() != null) {
            for (RoadmapPartRequest pr : request.getParts()) {
                RoadmapPart part = RoadmapPart.create(pr.getTitle(), pr.getOrder());
                part.setRoadmap(roadmap);
                if (pr.getTasks() != null) {
                    for (RoadmapTaskRequest tr : pr.getTasks()) {
                        part.addTask(RoadmapTask.create(
                                tr.getTitle(), tr.getDescription(),
                                parseDate(tr.getStartDate()), parseDate(tr.getEndDate()),
                                tr.getStartTime(), tr.getEndTime(), tr.getOrder()));
                    }
                }
                roadmap.getParts().add(part);
            }
        }
        return new RoadmapCreateResponse(roadmapRepository.save(roadmap));
    }

    @Transactional
    public RoadmapCreateResponse updateRoadmap(UUID curriculumId, RoadmapUpdateRequest request, String email) {
        Curriculum curriculum = getCurriculum(curriculumId, email);
        Roadmap roadmap = roadmapRepository.findByCurriculum(curriculum)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROADMAP_NOT_FOUND));
        List<RoadmapPart> newParts = buildParts(request.getParts());
        roadmap.update(request.getTitle(), newParts);
        return new RoadmapCreateResponse(roadmap);
    }

    @Transactional
    public void deleteRoadmap(UUID curriculumId, String email) {
        Curriculum curriculum = getCurriculum(curriculumId, email);
        Roadmap roadmap = roadmapRepository.findByCurriculum(curriculum)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROADMAP_NOT_FOUND));
        roadmapRepository.delete(roadmap);
    }

    @Transactional
    public TaskUpdateResponse updateTaskCompletion(UUID curriculumId, Long taskId, TaskUpdateRequest request, String email) {
        getCurriculum(curriculumId, email);
        RoadmapTask task = roadmapTaskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TASK_NOT_FOUND));
        task.updateCompleted(request.isCompleted());
        return new TaskUpdateResponse(task);
    }

    private Curriculum getCurriculum(UUID curriculumId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return curriculumRepository.findByIdAndUser(curriculumId, user)
                .orElseThrow(() -> new BusinessException(ErrorCode.CURRICULUM_NOT_FOUND));
    }

    private List<RoadmapPart> buildParts(List<RoadmapPartRequest> partRequests) {
        if (partRequests == null) return List.of();
        return partRequests.stream().map(pr -> {
            RoadmapPart part = RoadmapPart.create(pr.getTitle(), pr.getOrder());
            if (pr.getTasks() != null) {
                pr.getTasks().forEach(tr -> part.addTask(RoadmapTask.create(
                        tr.getTitle(), tr.getDescription(),
                        parseDate(tr.getStartDate()), parseDate(tr.getEndDate()),
                        tr.getStartTime(), tr.getEndTime(), tr.getOrder())));
            }
            return part;
        }).toList();
    }

    private LocalDate parseDate(String date) {
        return date != null ? LocalDate.parse(date) : null;
    }
}
