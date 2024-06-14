package com.example.memetory.domain.member.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class DeniedAccessException extends BusinessException {
	public DeniedAccessException() {
		super(ErrorCode.MEMBER_ACCESS_DENY);
	}
}
