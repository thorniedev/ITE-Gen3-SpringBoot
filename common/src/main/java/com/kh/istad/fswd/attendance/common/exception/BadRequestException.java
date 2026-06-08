package com.kh.istad.fswd.attendance.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApplicationException{
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, "BAD_REQUEST", message);
    }
}
