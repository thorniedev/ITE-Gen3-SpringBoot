package co.istad.ite_spring.exception;

public class DuplicateResourceException extends RuntimeException
{
    public DuplicateResourceException(String message) {
        super(message);
    }
}
