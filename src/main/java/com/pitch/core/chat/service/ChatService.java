package com.pitch.core.chat.service;

import com.pitch.core.chat.dto.ChatCreateRequest;
import com.pitch.core.chat.dto.ChatCreateResponse;
import com.pitch.core.chat.dto.ChatListResponse;
import com.pitch.core.chat.dto.ChatSummaryDto;
import com.pitch.core.chat.dto.ChatUpdateRequest;
import com.pitch.core.chat.dto.ChatUpdateResponse;
import com.pitch.core.chat.entity.Chat;
import com.pitch.core.chat.repository.ChatRepository;
import com.pitch.core.chat.repository.CurriculumRepository;
import com.pitch.core.chat.repository.MessageRepository;
import com.pitch.core.file.entity.FileEntity;
import com.pitch.core.file.repository.FileRepository;
import com.pitch.core.file.storage.StorageService;
import com.pitch.core.global.exception.BusinessException;
import com.pitch.core.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 채팅 목록 조회·생성·수정·삭제 비즈니스 로직
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private static final int MAX_LIMIT = 100;

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final CurriculumRepository curriculumRepository;
    private final FileRepository fileRepository;
    private final StorageService storageService;

    /**
     * 1) fileIds 검증(존재·소유권·미연결) → 2) 채팅 저장 → 3) 파일 chat_id 세팅
     * 갓 생성된 채팅이므로 messageCount=0, hasCurriculum=false를 고정으로 반환한다.
     */
    @Transactional
    public ChatCreateResponse create(UUID userId, ChatCreateRequest request) {
        List<UUID> rawIds = request.fileIds() == null ? List.of() : request.fileIds();
        // 중복 fileId 제거 (같은 ID를 여러 번 넘겨도 정상 처리)
        List<UUID> uniqueFileIds = rawIds.stream().distinct().toList();

        List<FileEntity> files = List.of();
        if (!uniqueFileIds.isEmpty()) {
            files = fileRepository.findAllByIdInAndUserId(uniqueFileIds, userId);

            // 결과 수 불일치 → 없는 파일이거나 타인 소유 (정보 노출 방지를 위해 통일)
            if (files.size() != uniqueFileIds.size()) {
                throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
            }

            // 이미 다른 채팅에 연결된 파일 존재 여부 확인
            boolean alreadyLinked = files.stream().anyMatch(f -> f.getChatId() != null);
            if (alreadyLinked) {
                throw new BusinessException(ErrorCode.FILE_ALREADY_LINKED);
            }
        }

        // saveAndFlush: INSERT를 즉시 실행해 @CreationTimestamp/@UpdateTimestamp가 엔티티에 반영되도록 함
        Chat chat = chatRepository.saveAndFlush(Chat.builder()
                .userId(userId)
                .title(request.title())
                .build());

        // JPA dirty checking으로 트랜잭션 커밋 시 UPDATE 자동 발행
        files.forEach(f -> f.linkToChat(chat.getId()));

        return new ChatCreateResponse(
                chat.getId(), chat.getTitle(), chat.getCreatedAt(), chat.getUpdatedAt(),
                0L, false
        );
    }

    /**
     * 연관 데이터를 FK 순서(files→messages→curriculums→chats)대로 삭제.
     * DB 삭제는 한 트랜잭션으로 원자적으로 커밋하고, 디스크 파일 삭제는
     * 커밋 후 실행(TransactionSynchronization.afterCommit)하여 DB 정합성을 최우선으로 보장.
     * 디스크 삭제 실패는 고아 파일로 남기고 경고 로그만 남긴다.
     */
    @Transactional
    public void delete(UUID chatId, UUID userId) {
        Chat chat = chatRepository.findByIdAndUserId(chatId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_NOT_FOUND));

        // storageKey 목록을 DB 삭제 전에 수집
        List<String> storageKeys = fileRepository.findAllByChatId(chatId).stream()
                .map(FileEntity::getStorageKey)
                .toList();

        // FK 자식 먼저 삭제 후 부모(chat) 삭제
        fileRepository.deleteByChatId(chatId);
        messageRepository.deleteByChatId(chatId);
        curriculumRepository.deleteByChatId(chatId);
        chatRepository.delete(chat);

        // 트랜잭션 커밋 후 디스크 삭제 — 디스크는 DB 커밋이 진실의 원천
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for (String key : storageKeys) {
                    try {
                        storageService.delete(key);
                    } catch (IOException e) {
                        log.warn("디스크 파일 삭제 실패 (고아 파일로 남음): key={}", key, e);
                    }
                }
            }
        });
    }

    /**
     * 소유권 확인 후 title 갱신.
     * saveAndFlush로 UPDATE를 즉시 실행해 @UpdateTimestamp가 엔티티에 반영되도록 함
     * (채팅 생성 시 createdAt null 이슈와 동일 계열).
     */
    @Transactional
    public ChatUpdateResponse updateTitle(UUID chatId, UUID userId, ChatUpdateRequest request) {
        Chat chat = chatRepository.findByIdAndUserId(chatId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_NOT_FOUND));
        chat.updateTitle(request.title());
        chatRepository.saveAndFlush(chat);
        return new ChatUpdateResponse(chat.getId(), chat.getTitle(), chat.getUpdatedAt());
    }

    /**
     * 본인 채팅 목록을 updated_at 내림차순으로 반환한다.
     * page는 1-base (Spring Data Pageable 변환 시 page-1 적용).
     */
    public ChatListResponse getChatList(UUID userId, int page, int limit) {
        // page < 1, limit < 1, limit > 100 검증
        if (page < 1 || limit < 1 || limit > MAX_LIMIT) {
            throw new BusinessException(ErrorCode.INVALID_PAGINATION);
        }

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<ChatSummaryDto> result = chatRepository.findChatSummariesByUserId(userId, pageable);

        List<ChatListResponse.ChatItem> chatItems = result.getContent().stream()
                .map(dto -> new ChatListResponse.ChatItem(
                        dto.getId(), dto.getTitle(), dto.getCreatedAt(), dto.getUpdatedAt(),
                        dto.getMessageCount(), dto.isHasCurriculum()))
                .toList();

        return new ChatListResponse(
                chatItems,
                new ChatListResponse.Pagination(page, limit, result.getTotalElements())
        );
    }
}
