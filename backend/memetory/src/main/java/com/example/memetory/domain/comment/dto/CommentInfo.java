package com.example.memetory.domain.comment.dto;

import com.example.memetory.domain.comment.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(description = "댓글 정보")
public class CommentInfo {

    @Schema(description = "댓글 아이디")
    private Long commentId;

    @Schema(description = "댓글 쓴 멤버 아이디")
    private Long memberId;

    @Schema(description = "댓글 쓴 멤버 이름")
    private String memberName;

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "댓글 생성 시각")
    private LocalDateTime createdAt;

    @Builder
    public CommentInfo(Long commentId, Long memberId, String memberName, String content, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static CommentInfo of(Comment comment) {
        return CommentInfo.builder()
                .commentId(comment.getId())
                .memberId(comment.getMember().getId())
                .memberName(comment.getMember().getName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
