package com.pitch.core.auth.service;

import com.pitch.core.auth.dto.AuthResponse;
import com.pitch.core.auth.dto.LoginRequest;
import com.pitch.core.auth.dto.LoginUserDto;
import com.pitch.core.auth.dto.LogoutRequest;
import com.pitch.core.auth.dto.RefreshRequest;
import com.pitch.core.auth.dto.SignupRequest;
import com.pitch.core.auth.dto.SignupUserDto;
import com.pitch.core.auth.dto.TokenResponse;
import com.pitch.core.auth.entity.RefreshToken;
import com.pitch.core.auth.repository.RefreshTokenRepository;
import com.pitch.core.global.config.JwtConfig;
import com.pitch.core.global.exception.BusinessException;
import com.pitch.core.global.exception.ErrorCode;
import com.pitch.core.global.jwt.JwtProvider;
import com.pitch.core.user.entity.User;
import com.pitch.core.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * 인증/인가 관련 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtConfig jwtConfig;

    /**
     * 회원가입을 처리하고 액세스/리프레시 토큰을 발급
     */
    public AuthResponse<SignupUserDto> signup(SignupRequest request) {
        // 1. 이메일 중복 체크
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 2. 비밀번호 BCrypt 인코딩 후 사용자 저장
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .build();
        userRepository.saveAndFlush(user);

        // 3. 액세스/리프레시 토큰 발급
        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getEmail());

        // 4. 리프레시 토큰 DB 저장
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiryDate(Instant.now().plus(Duration.ofMillis(jwtConfig.getRefreshTokenExpiration())))
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        return AuthResponse.<SignupUserDto>builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(SignupUserDto.from(user))
                .build();
    }

    /**
     * 로그인을 처리하고 액세스/리프레시 토큰을 발급
     */
    public AuthResponse<LoginUserDto> login(LoginRequest request) {
        // 1. 이메일로 사용자 조회 (계정 없음/비밀번호 불일치를 구분하지 않음)
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 3. 마지막 활동 시각 갱신
        user.updateLastActiveAt(Instant.now());
        userRepository.saveAndFlush(user);

        // 4. 액세스/리프레시 토큰 발급
        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getEmail());
        Instant expiryDate = Instant.now().plus(Duration.ofMillis(jwtConfig.getRefreshTokenExpiration()));

        // 5. 리프레시 토큰 갱신 또는 신규 저장
        refreshTokenRepository.findByUser(user)
                .ifPresentOrElse(
                        existing -> existing.update(refreshToken, expiryDate),
                        () -> refreshTokenRepository.save(RefreshToken.builder()
                                .user(user)
                                .token(refreshToken)
                                .expiryDate(expiryDate)
                                .build())
                );

        return AuthResponse.<LoginUserDto>builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(LoginUserDto.from(user))
                .build();
    }

    /**
     * 리프레시 토큰으로 액세스/리프레시 토큰을 재발급 (Refresh Token Rotation)
     */
    public TokenResponse refresh(RefreshRequest request) {
        // 1. 리프레시 토큰 검증/파싱
        Claims claims;
        try {
            claims = jwtProvider.parseClaims(request.refreshToken());
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        UUID userId = jwtProvider.getUserId(claims);
        String email = jwtProvider.getEmail(claims);

        // 2. DB에 저장된 리프레시 토큰 조회
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUser_Id(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN));

        // 3. 전달받은 토큰과 DB 저장값 일치 여부 확인 (이미 폐기된 토큰 재사용 방지)
        if (!refreshTokenEntity.getToken().equals(request.refreshToken())) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 4. 새 액세스/리프레시 토큰 발급
        String newAccessToken = jwtProvider.createAccessToken(userId, email);
        String newRefreshToken = jwtProvider.createRefreshToken(userId, email);
        Instant expiryDate = Instant.now().plus(Duration.ofMillis(jwtConfig.getRefreshTokenExpiration()));

        // 5. 기존 리프레시 토큰을 새 토큰으로 갱신 (rotation)
        refreshTokenEntity.update(newRefreshToken, expiryDate);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    /**
     * 로그아웃 처리 (리프레시 토큰 삭제로 재발급 경로를 차단)
     */
    public void logout(UUID userId, LogoutRequest request) {
        // 1. 전달받은 리프레시 토큰으로 DB 조회 (없으면 멱등하게 종료)
        refreshTokenRepository.findByToken(request.refreshToken())
                .ifPresent(refreshTokenEntity -> {
                    // 2. 소유권 검증: 인증된 사용자의 토큰이 아니면 거부
                    if (!refreshTokenEntity.getUser().getId().equals(userId)) {
                        throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
                    }

                    // 3. 본인 소유 토큰이면 하드 삭제
                    refreshTokenRepository.delete(refreshTokenEntity);
                });
    }
}
