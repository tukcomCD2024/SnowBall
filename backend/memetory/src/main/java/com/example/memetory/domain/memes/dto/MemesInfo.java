package com.example.memetory.domain.memes.dto;

import com.example.memetory.domain.memes.entity.Memes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(description = "밈스 정보")
public class MemesInfo {

    @Schema(description = "밈스 아이디")
    private Long memesId;

    @Schema(description = "밈스를 생성한 멤버 아이디")
    private Long memberId;

    @Schema(description = "밈스로 보여줄 밈의 S3 주소")
    private String memeUrl;

    @Schema(description = "밈스 제목")
    private String title;

    @Schema(description = "댓글 수")
    private int commentCount;

    @Schema(description = "좋아요 수")
    private int likeCount;

    @Schema(description = "밈스 생성 시각")
    private LocalDateTime createdAt;

    @Builder
    public MemesInfo(Long memesId, Long memberId, String memeUrl, String title, int commentCount, int likeCount, LocalDateTime createdAt) {
        this.memesId = memesId;
        this.memberId = memberId;
        this.memeUrl = memeUrl;
        this.title = title;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
    }

    public static MemesInfo of(Memes memes) {
        return MemesInfo.builder()
                .memesId(memes.getId())
                .memberId(memes.getMember().getId())
                .memeUrl(memes.getMeme().getS3Url())
                .title(memes.getTitle())
                .commentCount(memes.getCommentCount())
                .likeCount(memes.getLikeCount())
                .createdAt(memes.getCreatedAt())
                .build();
    }
}
