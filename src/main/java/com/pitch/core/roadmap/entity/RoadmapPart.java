package com.pitch.core.roadmap.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roadmap_parts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoadmapPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    @Setter
    private Roadmap roadmap;

    @Column(nullable = false)
    private String title;

    @Column(name = "sort_order")
    private int sortOrder;

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<RoadmapTask> tasks = new ArrayList<>();

    public static RoadmapPart create(String title, int order) {
        RoadmapPart p = new RoadmapPart();
        p.title = title;
        p.sortOrder = order;
        return p;
    }

    public void addTask(RoadmapTask task) {
        tasks.add(task);
        task.setPart(this);
    }
}
