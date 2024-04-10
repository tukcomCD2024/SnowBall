package com.example.memetory.domain.voice.dto.request;

import com.example.memetory.domain.voice.dto.VoiceServiceDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenerateVoiceRequestDto {
    private String s3Key;
    private String name;
    private String description;

    public VoiceServiceDto toServiceDtoS3(String email) {
        return VoiceServiceDto.builder()
                .email(email)
                .s3Key(s3Key)
                .name(name)
                .description(description)
                .build();
    }

    public VoiceServiceDto toServiceDtoElevenlabs(String email, String elevenlabsId) {
        return VoiceServiceDto.builder()
                .email(email)
                .elevenlabsVoiceId(elevenlabsId)
                .build();
    }
}
