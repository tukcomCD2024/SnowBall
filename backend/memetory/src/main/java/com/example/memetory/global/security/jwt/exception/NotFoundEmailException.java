package com.example.memetory.global.security.jwt.exception;

public class NotFoundEmailException extends RuntimeException{
	public NotFoundEmailException() {
	}

	public NotFoundEmailException(String message) {
		super(message);
	}

	public NotFoundEmailException(String message, Throwable cause) {
		super(message, cause);
	}
}
