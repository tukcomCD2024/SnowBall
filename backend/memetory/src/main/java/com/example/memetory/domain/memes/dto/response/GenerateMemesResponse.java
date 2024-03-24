package com.example.memetory.domain.memes.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GenerateMemesResponse {
    private Long memesId;
}
