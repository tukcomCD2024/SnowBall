package com.example.memetory.domain.voice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ElevenlabsVoiceResponse {

    @JsonProperty("voice_id")
    private String elevenlabsVoiceId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
}
