package com.example.memetory.domain.meme.dto;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.entity.Meme;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShotStackCallBackRequest {
	private String status;
	private String url;
	private String error;

	public Meme toEntity(Member member) {
		return Meme.builder()
			.s3Url(this.url)
			.member(member)
			.build();
	}
}
