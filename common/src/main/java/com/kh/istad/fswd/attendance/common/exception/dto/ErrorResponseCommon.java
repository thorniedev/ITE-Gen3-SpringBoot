package com.kh.istad.fswd.attendance.common.exception.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseCommon(
        int        status,
        String     errorCode,
        String     message,
        LocalDateTime timestamp,
        Map<String, String> fieldErrors
) {
    public static ErrorResponseCommon of(int status, String errorCode, String message) {
        return new ErrorResponseCommon(status, errorCode, message, LocalDateTime.now(), null);
    }

    public static ErrorResponseCommon ofValidation(String message, Map<String, String> fieldErrors) {
        return new ErrorResponseCommon(400, "VALIDATION_FAILED", message, LocalDateTime.now(), fieldErrors);
    }
}
