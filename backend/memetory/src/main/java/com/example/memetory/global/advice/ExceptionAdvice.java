package com.example.memetory.global.advice;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.member.exception.NotFoundMemberException;
import com.example.memetory.global.security.jwt.exception.NotFoundEmailException;
import com.example.memetory.global.security.jwt.exception.NotFoundTokenException;

@RestController
public class ExceptionAdvice {
	@ExceptionHandler(NotFoundTokenException.class)
	ResponseEntity<HttpEntity> notFoundTokenException() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ExceptionHandler(NotFoundEmailException.class)
	ResponseEntity<HttpEntity> notFoundEmailException() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ExceptionHandler(NotFoundMemberException.class)
	ResponseEntity<HttpEntity> notFoundMemberException() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
