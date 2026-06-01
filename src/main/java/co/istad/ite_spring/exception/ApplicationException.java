package co.istad.ite_spring.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ApplicationException
{

    //    @ExceptionHandler(MethodArgumentNotValidException.class)
    //    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //    public ResponseEntity <?> handleValidationException()
    //    {
    //        log.error("Validation Exception happened!!!!");
    //        Map<String, Object> response = new HashMap<>();
    //
    //        return ResponseEntity
    //                .badRequest() // 400
    //                .body(response);
    //    }

    // Handle Bad_Request
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request
    ){
        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "BAD REQUEST",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // Handle Not-Found
    @ExceptionHandler(ResourceNotfoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse  handleResourceNotFound(
            ResourceNotfoundException ex,
            HttpServletRequest request
    )
    {
        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "NOT FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // Handle Duplicate Resource
    public ValidationErrorResponse handleDuplicateResource(
            DuplicateResourceException ex,
            HttpServletRequest request
    ) {
        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Duplicate Resource",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

}

