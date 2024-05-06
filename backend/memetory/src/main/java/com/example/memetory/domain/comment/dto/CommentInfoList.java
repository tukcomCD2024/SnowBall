package com.example.memetory.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "댓글 리스트")
public class CommentInfoList {
    private List<CommentInfo> commentInfoList;

    @Builder
    public CommentInfoList(List<CommentInfo> commentInfoList) {
        this.commentInfoList = commentInfoList;
    }
}
