package com.example.memetory.domain.meme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemeServiceDto {
	private Long memberId;
	private String s3Url;
}
