package com.example.memetory.domain.meme.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "밈 생성 리스트 포맷")
public class GenerateMemeListRequest {

    @Schema(description = "3가지(소스 이미지, 타켓 이미지, 대사) 요소로 구성된 씬 리스트")
    private List<GenerateMemeDto> scene;

    public MemeServiceDto toServiceDto(String email) {
        return MemeServiceDto.builder()
                .email(email)
                .scene(scene)
                .build();
    }
}
