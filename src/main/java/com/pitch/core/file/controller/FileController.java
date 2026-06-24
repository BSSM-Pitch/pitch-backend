package com.pitch.core.file.controller;

import com.pitch.core.file.dto.FileUploadResponse;
import com.pitch.core.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * 파일 업로드 API 컨트롤러
 */
@Tag(name = "File")
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 문서 업로드 API (최대 3개, 파일당 250MB, 허용 형식: PDF/DOCX/TXT/PNG/JPEG/GIF/WEBP)
     */
    @Operation(summary = "문서 업로드")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FileUploadResponse upload(
            @RequestPart("files") List<MultipartFile> files,
            @AuthenticationPrincipal UUID userId
    ) {
        return fileService.upload(files, userId);
    }
}
