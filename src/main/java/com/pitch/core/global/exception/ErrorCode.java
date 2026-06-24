package com.pitch.core.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 비즈니스 로직에서 발생하는 에러 코드를 정의
 */
@Getter
public enum ErrorCode {

    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS", "이미 가입된 이메일입니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_EMAIL_FORMAT", "이메일 형식이 올바르지 않습니다."),
    WEAK_PASSWORD(HttpStatus.BAD_REQUEST, "WEAK_PASSWORD", "비밀번호는 8자 이상, 영문과 숫자를 포함해야 합니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "입력값이 올바르지 않습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", "유효하지 않은 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_REFRESH_TOKEN", "토큰이 만료되었습니다. 다시 로그인해주세요."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    CURRICULUM_NOT_FOUND(HttpStatus.NOT_FOUND, "CURRICULUM_NOT_FOUND", "해당 커리큘럼이 존재하지 않습니다."),
    ROADMAP_NOT_FOUND(HttpStatus.NOT_FOUND, "ROADMAP_NOT_FOUND", "해당 커리큘럼에 로드맵이 없습니다."),
    ROADMAP_ALREADY_EXISTS(HttpStatus.CONFLICT, "ROADMAP_ALREADY_EXISTS", "이미 로드맵이 존재합니다."),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "TASK_NOT_FOUND", "해당 작업이 존재하지 않습니다."),
    QUIZ_JOB_NOT_FOUND(HttpStatus.NOT_FOUND, "QUIZ_JOB_NOT_FOUND", "퀴즈 생성 작업을 찾을 수 없습니다."),
    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "QUIZ_NOT_FOUND", "해당 퀴즈가 존재하지 않습니다."),
    INVALID_TYPE(HttpStatus.BAD_REQUEST, "INVALID_TYPE", "존재하지 않는 문제 유형입니다."),
    INSUFFICIENT_LEARNING_CONTENT(HttpStatus.UNPROCESSABLE_ENTITY, "INSUFFICIENT_LEARNING_CONTENT", "퀴즈 생성에 충분한 학습 내용이 없습니다."),
    QUIZ_GENERATION_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "QUIZ_GENERATION_LIMIT_EXCEEDED", "퀴즈 생성 횟수를 초과했습니다.");
    // 파일 업로드
    TOO_MANY_FILES(HttpStatus.BAD_REQUEST, "TOO_MANY_FILES", "파일은 최대 3개까지 업로드할 수 있습니다."),
    EMPTY_FILE(HttpStatus.UNPROCESSABLE_ENTITY, "EMPTY_FILE", "빈 파일은 업로드할 수 없습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "INVALID_FILE_TYPE", "허용되지 않는 파일 형식입니다."),
    FILE_TOO_LARGE(HttpStatus.BAD_REQUEST, "FILE_TOO_LARGE", "파일 크기는 250MB를 초과할 수 없습니다."),

    // 채팅 목록 조회
    INVALID_PAGINATION(HttpStatus.BAD_REQUEST, "INVALID_PAGINATION", "page는 1 이상, limit는 1 이상 100 이하여야 합니다."),

    // 채팅 생성
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE_NOT_FOUND", "파일을 찾을 수 없습니다."),
    FILE_ALREADY_LINKED(HttpStatus.CONFLICT, "FILE_ALREADY_LINKED", "이미 다른 채팅에 연결된 파일입니다."),

    // 채팅 수정
    INVALID_TITLE(HttpStatus.BAD_REQUEST, "INVALID_TITLE", "제목은 비어 있을 수 없고 100자 이하여야 합니다."),
    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT_NOT_FOUND", "채팅을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
