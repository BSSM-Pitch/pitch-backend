package com.pitch.core.user.repository;

import com.pitch.core.user.entity.InstalledTool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * InstalledTool 엔티티에 대한 데이터 접근 레포지토리
 */
public interface InstalledToolRepository extends JpaRepository<InstalledTool, UUID> {

    List<InstalledTool> findByUserId(UUID userId);
}
