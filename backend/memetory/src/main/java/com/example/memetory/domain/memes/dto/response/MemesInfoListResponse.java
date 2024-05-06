package com.example.memetory.domain.memes.dto.response;

import com.example.memetory.domain.memes.dto.MemesInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "밈 리스트 응답 형식")
public class MemesInfoListResponse {

    @Schema(description = "밈 리스트")
    private List<MemesInfo> memesInfoList;

    @Builder
    public MemesInfoListResponse(List<MemesInfo> memesInfoList) {
        this.memesInfoList = memesInfoList;
    }
}
