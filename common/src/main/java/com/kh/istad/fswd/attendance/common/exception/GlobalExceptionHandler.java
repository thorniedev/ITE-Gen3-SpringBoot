package com.kh.istad.fswd.attendance.common.exception;

import com.kh.istad.fswd.attendance.common.exception.dto.ErrorResponseCommon;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseCommon> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage() == null
                                ? "Invalid value"
                                : fieldError.getDefaultMessage(),
                        (first, second) -> first
                ));

        return ResponseEntity.badRequest()
                .body(ErrorResponseCommon.ofValidation("Request validation failed", fieldErrors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseCommon> handleUnreadableMessage(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponseCommon.of(400, "BAD_REQUEST", "Request body is invalid JSON or has invalid field value"));
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
