package com.example.memetory.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Slice;

@Getter
@NoArgsConstructor
@Schema(description = "댓글 리스트")
public class CommentInfoSlice {
    @Schema(description = "현재 페이지")
    private int currentPage;
    @Schema(description = "다음 페이지 여부")
    private boolean hasNext;

    private List<CommentInfo> commentInfoList;

    @Builder
    public CommentInfoSlice(int currentPage, boolean hasNext, List<CommentInfo> commentInfoList) {
        this.currentPage = currentPage;
        this.hasNext = hasNext;
        this.commentInfoList = commentInfoList;
    }
}
