package com.example.memetory.global.security.jwt.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class NotFoundEmailException extends BusinessException {
	public NotFoundEmailException() {
		super(ErrorCode.EMAIL_NOT_FOUND);
	}
}
