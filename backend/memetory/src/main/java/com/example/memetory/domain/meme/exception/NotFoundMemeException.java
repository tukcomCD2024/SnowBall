package com.example.memetory.domain.meme.exception;

public class NotFoundMemeException extends RuntimeException{
	public NotFoundMemeException() {
	}

	public NotFoundMemeException(String message) {
		super(message);
	}

	public NotFoundMemeException(String message, Throwable cause) {
		super(message, cause);
	}
}
