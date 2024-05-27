package com.example.memetory.domain.like.dto;

import com.example.memetory.domain.like.entity.Like;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.memes.entity.Memes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LikeServiceDto {
    private Long memesId;
    private String email;

    public static LikeServiceDto fromEmailAndMemesId(String email, Long memesId) {
        return LikeServiceDto.builder()
                .email(email)
                .memesId(memesId)
                .build();
    }
}
