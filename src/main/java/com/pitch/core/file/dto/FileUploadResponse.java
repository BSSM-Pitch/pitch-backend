package com.pitch.core.file.dto;

import java.util.List;
import java.util.UUID;

public record FileUploadResponse(List<UploadedFile> uploadedFiles) {

    public record UploadedFile(UUID fileId, String name, Long size, String mimeType) {}
}
