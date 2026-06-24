package com.pitch.core.file.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 로컬 디스크에 파일을 저장하는 StorageService 구현체.
 * S3/Supabase로 교체 시 이 클래스만 교체하면 된다 (컨트롤러/서비스는 인터페이스에만 의존).
 */
@Service
public class LocalStorageService implements StorageService {

    private final Path basePath;

    public LocalStorageService(@Value("${storage.local.base-path}") String basePath) {
        this.basePath = Paths.get(basePath).toAbsolutePath().normalize();
    }

    @Override
    public String upload(MultipartFile file, String key) throws IOException {
        Path target = basePath.resolve(key).normalize();
        // path traversal 방지: 최종 경로가 basePath 하위인지 확인
        if (!target.startsWith(basePath)) {
            throw new IOException("유효하지 않은 저장 경로입니다: " + key);
        }
        Files.createDirectories(target.getParent());
        file.transferTo(target);
        return key;
    }

    @Override
    public void delete(String key) throws IOException {
        Path target = basePath.resolve(key).normalize();
        if (!target.startsWith(basePath)) {
            throw new IOException("유효하지 않은 저장 경로입니다: " + key);
        }
        Files.deleteIfExists(target);
    }
}
