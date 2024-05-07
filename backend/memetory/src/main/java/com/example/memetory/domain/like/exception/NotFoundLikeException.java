package com.example.memetory.domain.like.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class NotFoundLikeException extends BusinessException {
	public NotFoundLikeException() {
		super(ErrorCode.LIKE_NOT_FOUND);
	}
}
