package com.example.memetory.global.security.jwt.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class InvalidTokenException extends BusinessException {
	public InvalidTokenException() {
		super(ErrorCode.TOKEN_IS_INVALID);
	}
}
