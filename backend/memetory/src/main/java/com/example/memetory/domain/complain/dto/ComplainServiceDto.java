package com.example.memetory.domain.complain.dto;

import com.example.memetory.domain.complain.entity.Complain;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.memes.entity.Memes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ComplainServiceDto {
    private String email;
    private String content;
    private Long memesId;
    private Long complainId;

    public static ComplainServiceDto create(Long complainId) {
        return ComplainServiceDto.builder()
                .complainId(complainId)
                .build();
    }

    public Complain toEntity(Member member, Memes memes) {
        return Complain.builder()
                .content(content)
                .member(member)
                .memes(memes)
                .build();
    }
}
