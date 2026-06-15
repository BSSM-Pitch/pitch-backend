package com.pitch.core.user.controller;

import com.pitch.core.user.dto.UserMeResponse;
import com.pitch.core.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 사용자 관련 API 컨트롤러
 */
@Tag(name = "User")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 내 계정 정보 조회 API
     */
    @Operation(summary = "내 계정 정보 조회")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public UserMeResponse getMe(@AuthenticationPrincipal UUID userId) {
        return userService.getMe(userId);
    }
}
