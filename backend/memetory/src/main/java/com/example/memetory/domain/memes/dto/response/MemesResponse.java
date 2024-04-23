package com.example.memetory.domain.memes.dto.response;

import com.example.memetory.domain.comment.dto.CommentInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "밈스 단일 조회 반환 형식")
public class MemesResponse {
    @Schema(description = "밈스 아이디")
    private Long memesId;

    @Schema(description = "밈스를 생성한 멤버 아이디")
    private Long memberId;

    @Schema(description = "밈스를 생성한 멤버 이름")
    private String memberName;

    @Schema(description = "밈스로 보여줄 밈의 S3 주소")
    private String memeUrl;

    @Schema(description = "밈스 제목")
    private String title;

    @Schema(description = "댓글 수")
    private int commentCount;

    @Schema(description = "댓글 리스트")
    private List<CommentInfo> commentInfoList;

    @Schema(description = "좋아요 수")
    private int likeCount;

    @Schema(description = "밈스 생성 시각")
    private LocalDateTime createdAt;

    @Builder
    public MemesResponse(Long memesId, Long memberId, String memberName, String memeUrl, String title, int commentCount, List<CommentInfo> commentInfoList, int likeCount, LocalDateTime createdAt) {
        this.memesId = memesId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memeUrl = memeUrl;
        this.title = title;
        this.commentCount = commentCount;
        this.commentInfoList = commentInfoList;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
    }
}
