package com.pitch.core.file.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 파일 저장소 추상화 인터페이스.
 * 현재는 LocalStorageService가 구현체이며, 추후 S3StorageService 등으로 교체 가능.
 */
public interface StorageService {

    /**
     * 파일을 저장하고 스토리지 내 키를 반환한다.
     */
    String upload(MultipartFile file, String key) throws IOException;

    /**
     * 주어진 키에 해당하는 파일을 삭제한다.
     */
    void delete(String key) throws IOException;
}
