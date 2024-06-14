package com.example.memetory.domain.member.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class AccessDeniedException extends BusinessException {
	public AccessDeniedException() {
		super(ErrorCode.MEMBER_ACCESS_DENY);
	}
}
