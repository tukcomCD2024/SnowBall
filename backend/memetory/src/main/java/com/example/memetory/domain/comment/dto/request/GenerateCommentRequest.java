package com.example.memetory.domain.comment.dto.request;

import com.example.memetory.domain.comment.dto.CommentServiceDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GenerateCommentRequest {

    private Long memesId;

    private String content;

    public CommentServiceDto toServiceDto(String email) {
        return CommentServiceDto.builder()
                .memesId(memesId)
                .content(content)
                .email(email)
                .build();
    }
}
