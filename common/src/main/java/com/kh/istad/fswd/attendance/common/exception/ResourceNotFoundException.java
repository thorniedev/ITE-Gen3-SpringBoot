package com.kh.istad.fswd.attendance.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApplicationException
{

    public ResourceNotFoundException(String resourceName, Object id) {
        super(
                HttpStatus.NOT_FOUND,
                "RESOURCE_NOT_FOUND",
                resourceName + " not found with id: " + id
        );
    }

    public ResourceNotFoundException(String message){
        super(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", message);
    }
}
