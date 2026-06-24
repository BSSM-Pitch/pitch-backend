package com.pitch.core.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * 채팅당 0~1개로 존재하는 커리큘럼 엔티티 (존재 여부 판별용 최소 구조)
 */
@Entity
@Table(name = "curriculums")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curriculum {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    // curriculums 테이블이 FK를 보유하는 owning side
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false, unique = true)
    private Chat chat;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Builder
    private Curriculum(Chat chat) {
        this.chat = chat;
    }
}
