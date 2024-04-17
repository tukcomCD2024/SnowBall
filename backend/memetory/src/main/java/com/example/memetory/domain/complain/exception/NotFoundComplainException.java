package com.example.memetory.domain.complain.exception;

public class NotFoundComplainException extends RuntimeException{
    public NotFoundComplainException() {}

    public NotFoundComplainException(String message) {
        super(message);
    }

    public NotFoundComplainException(String message, Throwable cause) {
        super(message, cause);
    }
}
