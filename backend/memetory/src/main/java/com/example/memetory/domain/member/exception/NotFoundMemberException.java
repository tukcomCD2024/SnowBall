package com.example.memetory.domain.member.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class NotFoundMemberException extends BusinessException {
	public NotFoundMemberException() {
		super(ErrorCode.MEMBER_NOT_FOUND);
	}
}
