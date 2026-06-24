package com.pitch.core.chat.repository;

import com.pitch.core.chat.dto.ChatSummaryDto;
import com.pitch.core.chat.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

    /**
     * 한 방 쿼리로 채팅 목록과 집계값(messageCount, hasCurriculum)을 조회한다.
     * LEFT JOIN으로 messages·curriculums를 조인해 COUNT하므로 N+1이 발생하지 않는다.
     */
    /** 소유권 포함 단건 조회 — 없거나 타인 소유면 empty 반환 */
    Optional<Chat> findByIdAndUserId(UUID id, UUID userId);

    @Query(value = """
            SELECT new com.pitch.core.chat.dto.ChatSummaryDto(
                c.id, c.title, c.createdAt, c.updatedAt,
                COUNT(m.id), COUNT(cu.id)
            )
            FROM Chat c
            LEFT JOIN c.messages m
            LEFT JOIN c.curriculum cu
            WHERE c.userId = :userId
            GROUP BY c.id, c.title, c.createdAt, c.updatedAt
            ORDER BY c.updatedAt DESC
            """,
            countQuery = "SELECT COUNT(c) FROM Chat c WHERE c.userId = :userId")
    Page<ChatSummaryDto> findChatSummariesByUserId(@Param("userId") UUID userId, Pageable pageable);
}
