package com.example.memetory.domain.meme.dto;

import java.time.LocalDateTime;

import com.example.memetory.domain.meme.entity.Meme;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "밈 반환 포맷")
public class MemeResponse {

    @Schema(description = "밈 아이디")
    private Long memeId;

    @Schema(description = "밈이 저장된 S3 URL")
    private String s3Url;

    @Schema(description = "생성일시")
    private LocalDateTime createAt;

    @Schema(description = "수정일시")
    private LocalDateTime updateAt;

    @Builder
    public MemeResponse(Long memeId, String s3Url, LocalDateTime createAt, LocalDateTime updateAt) {
        this.memeId = memeId;
        this.s3Url = s3Url;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static MemeResponse of(Meme meme) {
        return MemeResponse.builder()
                .memeId(meme.getId())
                .s3Url(meme.getS3Url())
                .createAt(meme.getCreatedAt())
                .updateAt(meme.getUpdatedAt())
                .build();
    }
}
