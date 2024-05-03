package com.example.memetory.domain.complain.dto.request;

import com.example.memetory.domain.complain.dto.ComplainServiceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GenerateComplainRequest {
    private Long memesId;
    private String content;

    public ComplainServiceDto toServiceDto(String email) {
        return ComplainServiceDto.builder()
                .email(email)
                .memesId(memesId)
                .content(content)
                .build();
    }
}
