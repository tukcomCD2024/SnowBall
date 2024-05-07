package com.example.memetory.domain.complain.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class NotFoundComplainException extends BusinessException {
    public NotFoundComplainException() {
        super(ErrorCode.COMPLAIN_NOT_FOUND);
    }
}
