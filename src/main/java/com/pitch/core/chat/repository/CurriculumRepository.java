package com.pitch.core.chat.repository;

import com.pitch.core.chat.entity.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CurriculumRepository extends JpaRepository<Curriculum, UUID> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Curriculum cu WHERE cu.chat.id = :chatId")
    void deleteByChatId(@Param("chatId") UUID chatId);
}
