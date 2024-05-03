package com.example.memetory.domain.comment.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class NotFoundCommentException extends BusinessException {
    public NotFoundCommentException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}
