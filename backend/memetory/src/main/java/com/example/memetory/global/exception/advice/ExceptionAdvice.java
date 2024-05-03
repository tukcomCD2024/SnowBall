package com.example.memetory.global.exception.advice;

import static com.example.memetory.global.response.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;
import com.example.memetory.global.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error(e.getMessage(), e);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleRuntimeException(BusinessException e) {
		final ErrorCode errorCode = e.getErrorCode();
		final ErrorResponse response =
			ErrorResponse.builder()
				.errorMessage(errorCode.getMessage())
				.build();
		log.warn(e.getMessage());
		return ResponseEntity.status(errorCode.getStatus()).body(response);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {
		final ErrorResponse response = ErrorResponse.of(INPUT_INVALID_VALUE, e.getBindingResult());
		log.warn(e.getMessage());
		return new ResponseEntity<>(response, BAD_REQUEST);
	}
}
