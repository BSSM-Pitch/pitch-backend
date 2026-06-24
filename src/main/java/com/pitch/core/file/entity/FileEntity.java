package com.pitch.core.file.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * 업로드된 파일의 메타데이터를 저장하는 엔티티
 */
@Entity
@Table(name = "files")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long size;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    // 스토리지 교체 시 이 키만 바꾸면 되도록 구현체 경로/키를 분리해서 저장
    @Column(name = "storage_key", nullable = false)
    private String storageKey;

    // Chat 엔티티 직접 참조 대신 UUID만 저장 — 도메인 간 순환참조·직렬화 이슈 방지
    @Column(name = "chat_id")
    private UUID chatId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Builder
    private FileEntity(UUID userId, String name, Long size, String mimeType, String storageKey) {
        this.userId = userId;
        this.name = name;
        this.size = size;
        this.mimeType = mimeType;
        this.storageKey = storageKey;
    }

    /**
     * 파일을 채팅에 연결한다. JPA dirty checking으로 트랜잭션 커밋 시 UPDATE가 발행된다.
     */
    public void linkToChat(UUID chatId) {
        this.chatId = chatId;
    }
}
