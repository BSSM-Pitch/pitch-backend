package com.pitch.core.global.exception;

import com.pitch.core.global.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 컨트롤러 전역에서 발생하는 예외를 일괄 처리
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직 처리 중 발생한 예외 처리 (예: 이메일 중복)
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

    /**
     * Bean Validation(@Email, @Pattern 등) 실패 시 발생하는 예외 처리
     * 터진 필드에 따라 INVALID_EMAIL_FORMAT / WEAK_PASSWORD / INVALID_INPUT으로 매핑
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT));

        ErrorCode errorCode = switch (fieldError.getField()) {
            case "email" -> ErrorCode.INVALID_EMAIL_FORMAT;
            case "password" -> ErrorCode.WEAK_PASSWORD;
            default -> ErrorCode.INVALID_INPUT;
        };

        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

    /**
     * 그 외 예상하지 못한 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다."));
    }
}
