package com.example.memetory.domain.memes.dto.request;

import com.example.memetory.domain.memes.dto.MemesServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "밈스 생성 포맷")
public class GenerateMemesRequest {

    @Schema(description = "밈 아이디")
    private Long memeId;

    @Schema(description = "밈스 제목")
    private String title;

    public MemesServiceDto toServiceDto(String email) {
        return MemesServiceDto.builder()
                .email(email)
                .memeId(memeId)
                .title(title)
                .build();
    }
}
