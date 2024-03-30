package com.example.memetory.domain.meme.dto;

import java.util.List;

import com.example.memetory.domain.meme.entity.Meme;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "밈 리스트 반환 포맷")
public class MemeListResponse {

    @Schema(description = "밈 리스트")
    private List<MemeResponse> memeList;

    @Builder
    public MemeListResponse(List<MemeResponse> memeList) {
        this.memeList = memeList;
    }
}
