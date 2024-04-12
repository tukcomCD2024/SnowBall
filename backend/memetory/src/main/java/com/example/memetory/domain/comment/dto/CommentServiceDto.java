package com.example.memetory.domain.comment.dto;

import com.example.memetory.domain.comment.entity.Comment;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.dto.MemeServiceDto;
import com.example.memetory.domain.memes.entity.Memes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CommentServiceDto {
    private Long memesId;
    private Long commentId;
    private String content;
    private String email;

    public static CommentServiceDto create(Long commentId) {
        return CommentServiceDto.builder()
                .commentId(commentId)
                .build();
    }

    public Comment toEntity(Member member, Memes memes) {
        return Comment.builder()
                .content(content)
                .member(member)
                .memes(memes)
                .build();
    }
}
