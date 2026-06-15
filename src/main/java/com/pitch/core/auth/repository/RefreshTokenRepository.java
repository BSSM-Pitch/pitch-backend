package com.pitch.core.auth.repository;

import com.pitch.core.auth.entity.RefreshToken;
import com.pitch.core.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * RefreshToken 엔티티에 대한 데이터 접근 레포지토리
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findByUser_Id(UUID userId);

    Optional<RefreshToken> findByToken(String token);
}
