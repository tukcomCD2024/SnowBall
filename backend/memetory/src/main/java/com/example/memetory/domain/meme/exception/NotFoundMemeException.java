package com.example.memetory.domain.meme.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class NotFoundMemeException extends BusinessException {
	public NotFoundMemeException() {
		super(ErrorCode.MEME_NOT_FOUND);
	}
}
