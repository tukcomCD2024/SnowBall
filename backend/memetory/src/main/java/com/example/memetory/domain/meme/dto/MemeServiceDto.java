package com.example.memetory.domain.meme.dto;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.entity.Meme;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemeServiceDto {
	private Long memberId;
	private String s3Url;

	public Meme toEntity(Member member) {
		return Meme.builder()
			.member(member)
			.s3Url(this.s3Url)
			.build();
	}
}
