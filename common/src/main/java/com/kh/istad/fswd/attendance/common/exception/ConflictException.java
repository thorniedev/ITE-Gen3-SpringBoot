package com.kh.istad.fswd.attendance.common.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends ApplicationException{
    public ConflictException(String message){
        super(HttpStatus.CONFLICT, "CONFLICT!", message);
    }
}
