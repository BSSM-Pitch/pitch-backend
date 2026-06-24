package com.pitch.core.chat.controller;

import com.pitch.core.chat.dto.ChatCreateRequest;
import com.pitch.core.chat.dto.ChatCreateResponse;
import com.pitch.core.chat.dto.ChatListResponse;
import com.pitch.core.chat.dto.ChatUpdateRequest;
import com.pitch.core.chat.dto.ChatUpdateResponse;
import com.pitch.core.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 채팅 목록 조회 API 컨트롤러
 */
@Tag(name = "Chat")
@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "채팅 생성")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChatCreateResponse createChat(
            @RequestBody @Valid ChatCreateRequest request,
            @AuthenticationPrincipal UUID userId
    ) {
        return chatService.create(userId, request);
    }

    @Operation(summary = "채팅 삭제")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{chatId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChat(
            @PathVariable UUID chatId,
            @AuthenticationPrincipal UUID userId
    ) {
        chatService.delete(chatId, userId);
    }

    @Operation(summary = "채팅 제목 수정")
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{chatId}")
    public ChatUpdateResponse updateTitle(
            @PathVariable UUID chatId,
            @RequestBody @Valid ChatUpdateRequest request,
            @AuthenticationPrincipal UUID userId
    ) {
        return chatService.updateTitle(chatId, userId, request);
    }

    @Operation(summary = "채팅 목록 조회")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ChatListResponse getChatList(
            @Parameter(description = "페이지 번호 (1부터 시작, 기본값 1)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "페이지당 항목 수 (기본값 20, 최대 100)") @RequestParam(defaultValue = "20") int limit,
            @AuthenticationPrincipal UUID userId
    ) {
        return chatService.getChatList(userId, page, limit);
    }
}
