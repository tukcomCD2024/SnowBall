package com.example.memetory.domain.voice.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class NotFoundVoiceException extends BusinessException {
    public NotFoundVoiceException() {
        super(ErrorCode.VOICE_NOT_FOUND);
    }
}
