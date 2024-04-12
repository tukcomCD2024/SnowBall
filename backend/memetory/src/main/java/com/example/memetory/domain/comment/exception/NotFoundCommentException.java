package com.example.memetory.domain.comment.exception;


public class NotFoundCommentException extends RuntimeException {
    public NotFoundCommentException() {}

    public NotFoundCommentException(String message) {
        super(message);
    }

    public NotFoundCommentException(String message, Throwable cause) {
        super(message, cause);
    }
}
