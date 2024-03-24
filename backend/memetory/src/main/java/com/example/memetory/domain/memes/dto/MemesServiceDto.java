package com.example.memetory.domain.memes.dto;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.memes.entity.Memes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemesServiceDto {
    private Long memberId;
    private Long memeId;
    private String email;
    private String title;
    private int likeCount;
    private int commentCount;

    public Memes toEntity(Member member, Meme meme) {
        return Memes.builder()
                .member(member)
                .meme(meme)
                .title(this.title)
                .likeCount(0)
                .commentCount(0)
                .build();
    }
}
