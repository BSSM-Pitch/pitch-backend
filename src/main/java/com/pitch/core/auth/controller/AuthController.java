package com.pitch.core.auth.controller;

import com.pitch.core.auth.dto.AuthResponse;
import com.pitch.core.auth.dto.LoginRequest;
import com.pitch.core.auth.dto.LoginUserDto;
import com.pitch.core.auth.dto.LogoutRequest;
import com.pitch.core.auth.dto.RefreshRequest;
import com.pitch.core.auth.dto.SignupRequest;
import com.pitch.core.auth.dto.SignupUserDto;
import com.pitch.core.auth.dto.TokenResponse;
import com.pitch.core.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 인증 관련 API 컨트롤러
 */
@Tag(name = "Auth")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입 API
     */
    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse<SignupUserDto>> signup(@Valid @RequestBody SignupRequest request) {
        AuthResponse<SignupUserDto> response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 로그인 API
     */
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse<LoginUserDto>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse<LoginUserDto> response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 토큰 재발급 API
     */
    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        TokenResponse response = authService.refresh(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 로그아웃 API
     */
    @Operation(summary = "로그아웃")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@AuthenticationPrincipal UUID userId, @Valid @RequestBody LogoutRequest request) {
        authService.logout(userId, request);
    }
}
