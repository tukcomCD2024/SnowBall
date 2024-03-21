package com.example.memetory.domain.meme.dto;

import java.time.LocalDateTime;

import com.example.memetory.domain.meme.entity.Meme;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemeResponse {
	private Long memeId;
	private String s3Url;
	private LocalDateTime createAt;
	private LocalDateTime updateAt;

	@Builder
	public MemeResponse(Long memeId, String s3Url, LocalDateTime createAt, LocalDateTime updateAt) {
		this.memeId = memeId;
		this.s3Url = s3Url;
		this.createAt = createAt;
		this.updateAt = updateAt;
	}

	public static MemeResponse of(Meme meme) {
		return MemeResponse.builder()
			.memeId(meme.getId())
			.s3Url(meme.getS3Url())
			.createAt(meme.getCreatedAt())
			.updateAt(meme.getUpdatedAt())
			.build();
	}
}
