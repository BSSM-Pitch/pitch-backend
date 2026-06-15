package com.pitch.core.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 사용자가 설치한 도구 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "user_installed_tools")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InstalledTool {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String tool;

    @Builder
    private InstalledTool(User user, String tool) {
        this.user = user;
        this.tool = tool;
    }
}
