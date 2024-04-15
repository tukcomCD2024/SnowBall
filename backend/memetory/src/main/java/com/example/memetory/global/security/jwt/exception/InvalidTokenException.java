package com.example.memetory.global.security.jwt.exception;

public class InvalidTokenException extends RuntimeException {
	public InvalidTokenException() {
	}

	public InvalidTokenException(String message) {
		super(message);
	}

	public InvalidTokenException(String message, Throwable cause) {
		super(message, cause);
	}
}
