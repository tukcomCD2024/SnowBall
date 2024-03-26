package com.example.memetory.domain.like.exception;

public class NotFoundLikeException extends RuntimeException {
    public NotFoundLikeException(){}

    public NotFoundLikeException(String message) {
        super(message);
    }

    public NotFoundLikeException(String message, Throwable cause) {
        super(message, cause);
    }
}
