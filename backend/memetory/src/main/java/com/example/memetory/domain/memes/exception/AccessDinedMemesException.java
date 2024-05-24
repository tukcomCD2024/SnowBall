package com.example.memetory.domain.memes.exception;

import static com.example.memetory.global.response.ErrorCode.*;

import com.example.memetory.global.exception.BusinessException;

public class AccessDinedMemesException extends BusinessException {
	public AccessDinedMemesException() {
		super(MEMES_ACCESS_DENY);
	}
}
