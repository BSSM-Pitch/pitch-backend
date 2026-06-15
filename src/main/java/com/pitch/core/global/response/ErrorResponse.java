package com.pitch.core.global.response;

import com.pitch.core.global.exception.ErrorCode;

/**
 * 에러 응답 형식 { "code": "...", "message": "..." }
 */
public record ErrorResponse(String code, String message) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode.getCode(), message);
    }
}
