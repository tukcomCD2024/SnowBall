package com.example.memetory.domain.meme.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "밈 생성 후 콜백 받는 포맷")
public class ShotStackCallBackRequest {

    @Schema(description = "상태")
    private String status;

    @Schema(description = "생성된 밈의 S3 URL")
    private String url;

    @Schema(description = "에러")
    private String error;

    public MemeServiceDto toServiceDto(Long memberId) {
        return MemeServiceDto.builder()
                .memberId(memberId)
                .s3Url(url)
                .build();
    }
}
