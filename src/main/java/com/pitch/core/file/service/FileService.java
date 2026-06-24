package com.pitch.core.file.service;

import com.pitch.core.file.dto.FileUploadResponse;
import com.pitch.core.file.entity.FileEntity;
import com.pitch.core.file.repository.FileRepository;
import com.pitch.core.file.storage.StorageService;
import com.pitch.core.global.exception.BusinessException;
import com.pitch.core.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * 파일 업로드 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
public class FileService {

    private static final int MAX_FILE_COUNT = 3;
    private static final long MAX_FILE_SIZE_BYTES = 250L * 1024 * 1024; // 250MB

    // MIME 타입과 확장자를 모두 검사 (MIME만 믿으면 위조 가능)
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain",
            "image/png",
            "image/jpeg",
            "image/gif",
            "image/webp"
    );

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "pdf", "docx", "txt", "png", "jpg", "jpeg", "gif", "webp"
    );

    private final StorageService storageService;
    private final FileRepository fileRepository;

    /**
     * 1) 개수 검증 → 2) 파일별 검증 → 3) 스토리지 저장 → 4) DB 메타데이터 저장
     * 검증을 전부 먼저 끝내고 저장 단계로 진입해 부분 저장을 최소화한다.
     */
    @Transactional
    public FileUploadResponse upload(List<MultipartFile> files, UUID userId) {
        if (files.size() > MAX_FILE_COUNT) {
            throw new BusinessException(ErrorCode.TOO_MANY_FILES);
        }

        for (MultipartFile file : files) {
            validateFile(file);
        }

        List<String> uploadedKeys = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String key = userId + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
                storageService.upload(file, key);
                uploadedKeys.add(key);
            }
        } catch (IOException e) {
            cleanupPartialUploads(uploadedKeys);
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
        }

        List<FileEntity> entities = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            entities.add(FileEntity.builder()
                    .userId(userId)
                    .name(file.getOriginalFilename())
                    .size(file.getSize())
                    .mimeType(file.getContentType())
                    .storageKey(uploadedKeys.get(i))
                    .build());
        }
        fileRepository.saveAll(entities);

        List<FileUploadResponse.UploadedFile> uploadedFiles = entities.stream()
                .map(e -> new FileUploadResponse.UploadedFile(e.getId(), e.getName(), e.getSize(), e.getMimeType()))
                .toList();

        return new FileUploadResponse(uploadedFiles);
    }

    // 검증 순서: 빈 파일 → 형식 → 크기
    private void validateFile(MultipartFile file) {
        if (file.isEmpty() || file.getSize() == 0) {
            throw new BusinessException(ErrorCode.EMPTY_FILE);
        }

        String mimeType = file.getContentType();
        String ext = extractExtension(file.getOriginalFilename()).toLowerCase(Locale.ROOT);
        if (!ALLOWED_MIME_TYPES.contains(mimeType) || !ALLOWED_EXTENSIONS.contains(ext)) {
            throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new BusinessException(ErrorCode.FILE_TOO_LARGE);
        }
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    private void cleanupPartialUploads(List<String> keys) {
        for (String key : keys) {
            try {
                storageService.delete(key);
            } catch (IOException ignored) {
            }
        }
    }
}
