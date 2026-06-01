package co.istad.ite_spring.exception;

import java.time.LocalDateTime;

public record ValidationErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
