package com.example.memetory.domain.voice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenerateVoiceResponseDto {

    @JsonProperty("voice_id")
    private String elevenlabsVoiceId;

}
