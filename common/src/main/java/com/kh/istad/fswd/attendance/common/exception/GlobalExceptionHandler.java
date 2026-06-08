package com.kh.istad.fswd.attendance.common.exception;

import com.kh.istad.fswd.attendance.common.exception.dto.ErrorResponseCommon;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponseCommon> handleExceptionApplication(ApplicationException ex)
    {
        return build(ex);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseCommon> handleNotFoundException(
            ResourceNotFoundException ex
    ) {
        return build(ex);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseCommon> handleConflictException(
            ConflictException ex
    ) {
        return build(ex);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseCommon> handleBadRequestException(BadRequestException ex)
    {
        return build(ex);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseCommon> handleBadForbiddenException(ForbiddenException ex)
    {
        return build(ex);
    }


    // Helper
    private ResponseEntity<ErrorResponseCommon> build(ApplicationException ex) {
        ErrorResponseCommon body = ErrorResponseCommon.of(
                ex.getStatus().value(),
                ex.getErrorCode(),
                ex.getMessage()
        );
        return ResponseEntity.status(ex.getStatus()).body(body);
    }
}
