package com.example.memetory.domain.meme.dto;

import com.google.gson.annotations.SerializedName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "밈 생성 포맷")
public class GenerateMemeDto {
    @Schema(description = "적용하고 싶은 이미지")
    @SerializedName("source_image")
    private String sourceImage;

    @Schema(description = "배경 이미지")
    @SerializedName("target_image")
    private String targetImage;

    @Schema(description = "대사")
    @SerializedName("text")
    private String text;
  
    @Schema(description = "일레븐랩스 목소리 ID")
    @SerializedName("voice_id")
  	private String voiceId;
}
