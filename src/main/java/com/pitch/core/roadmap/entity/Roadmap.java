package com.pitch.core.roadmap.entity;

import com.pitch.core.curriculum.entity.Curriculum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roadmaps")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_id", nullable = false, unique = true)
    private Curriculum curriculum;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<RoadmapPart> parts = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public static Roadmap create(Curriculum curriculum, String title) {
        Roadmap r = new Roadmap();
        r.curriculum = curriculum;
        r.title = title;
        return r;
    }

    public void update(String title, List<RoadmapPart> newParts) {
        this.title = title;
        this.parts.clear();
        newParts.forEach(p -> p.setRoadmap(this));
        this.parts.addAll(newParts);
    }
}
