package com.example.memetory.domain.like.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class NotCreateLikeException extends BusinessException {

	public NotCreateLikeException() {
		super(ErrorCode.LIKE_NOT_CREATE);
	}
}
