package com.example.memetory.domain.memes.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class NotFoundMemesException extends BusinessException {
    public NotFoundMemesException(String message) {
        super(ErrorCode.MEMES_NOT_FOUND);
    }
}
