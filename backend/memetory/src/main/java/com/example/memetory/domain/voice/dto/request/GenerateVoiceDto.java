package com.example.memetory.domain.voice.dto.request;

import com.example.memetory.domain.voice.dto.VoiceServiceDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenerateVoiceDto {
    private String s3Url;

    public VoiceServiceDto toServiceDto(String email) {
        return VoiceServiceDto.builder()
                .email(email)
                .s3Url(s3Url)
                .build();
    }
}
