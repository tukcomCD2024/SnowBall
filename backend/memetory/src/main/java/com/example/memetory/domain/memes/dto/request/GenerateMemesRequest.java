package com.example.memetory.domain.memes.dto.request;

import com.example.memetory.domain.memes.dto.MemesServiceDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenerateMemesRequest {

    private Long memeId;
    private String title;

    public MemesServiceDto toServiceDto(String email) {
        return MemesServiceDto.builder()
                .email(email)
                .memeId(memeId)
                .title(title)
                .build();
    }
}
