package com.example.memetory.global.security.jwt.exception;

public class NotFoundTokenException extends RuntimeException {
	public NotFoundTokenException() {
	}

	public NotFoundTokenException(String message) {
		super(message);
	}

	public NotFoundTokenException(String message, Throwable cause) {
		super(message, cause);
	}
}
