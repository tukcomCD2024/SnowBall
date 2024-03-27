package com.example.memetory.domain.memes.exception;

public class NotFoundMemesException extends RuntimeException {
    public NotFoundMemesException() {}

    public NotFoundMemesException(String message) {
        super(message);
    }

    public NotFoundMemesException(String message, Throwable cause) {
        super(message, cause);
    }
}
