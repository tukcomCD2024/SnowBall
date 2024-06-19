package com.example.memetory.domain.voice.exception;

import com.example.memetory.global.exception.BusinessException;
import com.example.memetory.global.response.ErrorCode;

public class AlreadyExistVoiceException extends BusinessException {
    public AlreadyExistVoiceException() {
        super(ErrorCode.VOICE_ALREADY_EXIST);
    }
}
