package com.pitch.core.global.exception;

import lombok.Getter;

/**
 * 비즈니스 로직 처리 중 발생하는 예외
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
