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
    private Long memesId;
    private String email;
    private String title;
    private Long likeCount;
    private Long commentCount;

    public static MemesServiceDto create(Long memesId) {
        return MemesServiceDto.builder()
                .memesId(memesId)
                .build();
    }


    public static MemesServiceDto create(Long memesId, String email) {
        return MemesServiceDto.builder()
            .memesId(memesId)
            .email(email)
            .build();
    }

    public Memes toEntity(Member member, Meme meme) {
        return Memes.builder()
                .member(member)
                .meme(meme)
                .title(this.title)
                .likeCount(0L)
                .commentCount(0L)
                .build();
    }
}
