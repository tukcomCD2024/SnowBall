package com.example.memetory.domain.memes.exception;

import static com.example.memetory.global.response.ErrorCode.*;

import com.example.memetory.global.exception.BusinessException;

public class NotDeleteMemesException extends BusinessException {
	public NotDeleteMemesException() {
		super(MEMES_NOT_DELETE);
	}
}
