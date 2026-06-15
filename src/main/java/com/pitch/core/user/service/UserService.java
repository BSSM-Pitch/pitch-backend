package com.pitch.core.user.service;

import com.pitch.core.global.exception.BusinessException;
import com.pitch.core.global.exception.ErrorCode;
import com.pitch.core.user.dto.UserMeResponse;
import com.pitch.core.user.entity.InstalledTool;
import com.pitch.core.user.entity.User;
import com.pitch.core.user.repository.InstalledToolRepository;
import com.pitch.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * 사용자 정보 관련 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final InstalledToolRepository installedToolRepository;

    /**
     * 내 계정 정보와 설치한 도구 목록을 조회
     */
    public UserMeResponse getMe(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        List<String> installedTools = installedToolRepository.findByUserId(userId).stream()
                .map(InstalledTool::getTool)
                .toList();

        return UserMeResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .installedTools(installedTools)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
