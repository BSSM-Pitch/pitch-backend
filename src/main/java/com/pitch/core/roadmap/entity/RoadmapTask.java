package com.pitch.core.roadmap.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "roadmap_tasks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoadmapTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    @Setter
    private RoadmapPart part;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;
    private String startTime;
    private String endTime;
    private boolean completed;

    @Column(name = "sort_order")
    private int sortOrder;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public static RoadmapTask create(String title, String description,
                                     LocalDate startDate, LocalDate endDate,
                                     String startTime, String endTime, int order) {
        RoadmapTask t = new RoadmapTask();
        t.title = title;
        t.description = description;
        t.startDate = startDate;
        t.endDate = endDate;
        t.startTime = startTime;
        t.endTime = endTime;
        t.sortOrder = order;
        return t;
    }

    public void updateCompleted(boolean completed) {
        this.completed = completed;
    }
}
