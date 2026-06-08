package com.kh.istad.fswd.attendance.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApplicationException
{
    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, "FORBIDDEN", message);
    }

    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN, "FORBIDDEN", "You do not have permission to perform this action");
    }
}
