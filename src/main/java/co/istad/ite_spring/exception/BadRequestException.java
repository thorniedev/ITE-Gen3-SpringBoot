package co.istad.ite_spring.exception;

public class BadRequestException extends RuntimeException
{
    public BadRequestException(String message) {
        super(message);
    }
}
