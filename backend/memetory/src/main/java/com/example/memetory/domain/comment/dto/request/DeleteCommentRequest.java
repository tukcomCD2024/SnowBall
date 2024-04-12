package com.example.memetory.domain.comment.dto.request;

import com.example.memetory.domain.comment.dto.CommentServiceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteCommentRequest {

    private Long memesId;

    public CommentServiceDto toServiceDto(Long commentId) {
        return CommentServiceDto.builder()
                .memesId(memesId)
                .commentId(commentId)
                .build();
    }
}
