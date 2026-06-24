package com.pitch.core.file.repository;

import com.pitch.core.file.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {

    /**
     * ID 목록 중 지정 유저 소유인 파일만 반환한다.
     * 결과 수가 요청 수와 다르면 없거나 타인 소유 파일이 포함된 것이다.
     */
    List<FileEntity> findAllByIdInAndUserId(Collection<UUID> ids, UUID userId);

    /** 채팅에 연결된 파일 목록 조회 (storageKey 수집용) */
    List<FileEntity> findAllByChatId(UUID chatId);

    /** 채팅에 연결된 파일 일괄 삭제. clearAutomatically로 퍼시스턴스 컨텍스트 정합성 유지 */
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM FileEntity f WHERE f.chatId = :chatId")
    void deleteByChatId(@Param("chatId") UUID chatId);
}
