package com.example.memetory.global.security.jwt.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class NotFoundTokenException extends BusinessException {
	public NotFoundTokenException() {
		super(ErrorCode.TOKEN_NOT_FOUND);
	}
}
