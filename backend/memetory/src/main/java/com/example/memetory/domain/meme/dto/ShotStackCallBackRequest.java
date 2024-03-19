package com.example.memetory.domain.meme.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShotStackCallBackRequest {
	private String status;
	private String url;
	private String error;

	public MemeServiceDto toServiceDto(Long memberId) {
		return MemeServiceDto.builder()
			.memberId(memberId)
			.s3Url(url)
			.build();
	}
}
