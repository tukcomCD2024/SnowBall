package com.example.memetory.domain.comment.dto;

import com.example.memetory.domain.comment.entity.Comment;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.memes.entity.Memes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CommentServiceDto {
    private Long memesId;
    private String content;
    private String email;

    public Comment toEntity(Member member, Memes memes) {
        return Comment.builder()
                .content(content)
                .member(member)
                .memes(memes)
                .build();
    }
}
