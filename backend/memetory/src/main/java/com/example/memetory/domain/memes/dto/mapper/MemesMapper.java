package com.example.memetory.domain.memes.dto.mapper;

import com.example.memetory.domain.memes.dto.response.GenerateMemesResponse;
import com.example.memetory.domain.memes.entity.Memes;
import org.springframework.stereotype.Component;

@Component
public class MemesMapper {
    public GenerateMemesResponse EntityToGenerateMemesResponse(Memes memes) {
        return GenerateMemesResponse.builder()
                .memesId(memes.getId())
                .build();
    }
}
