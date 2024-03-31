package com.example.memetory.domain.voice.dto;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.voice.entity.Voice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VoiceServiceDto {

    private String email;
    private String s3Url;

    public Voice toEntity(Member member) {
        return Voice.builder()
                .member(member)
                .s3Url(s3Url)
                .build();
    }
}
