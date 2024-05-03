package com.example.memetory.domain.member.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class DuplicatedMemberException extends BusinessException {
	public DuplicatedMemberException() {
		super(ErrorCode.NICKNAME_IS_DUPLICATED);
	}
}
