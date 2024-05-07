package com.example.memetory.domain.meme.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class AccessDeniedMemeException extends BusinessException {
	public AccessDeniedMemeException() {
		super(ErrorCode.MEME_ACCESS_DENY);
	}
}
