package com.kh.istad.fswd.attendance.common.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException
{
    private final HttpStatus status;
    private final String errorCode;

    public ApplicationException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status    = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus()    { return status; }
    public String     getErrorCode() { return errorCode; }
}

