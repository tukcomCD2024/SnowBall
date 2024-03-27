package com.example.memetory.domain.meme.dto;

import java.util.List;

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
	private Long memeId;
	private String s3Url;
	private String email;
	private List<GenerateMemeDto> scene;

	public static MemeServiceDto create(String email, Long memeId) {
		return MemeServiceDto.builder()
			.email(email)
			.memeId(memeId)
			.build();
	}

	public static MemeServiceDto create(String email) {
		return MemeServiceDto.builder()
			.email(email)
			.build();
	}

	public Meme toEntity(Member member) {
		return Meme.builder()
			.member(member)
			.s3Url(this.s3Url)
			.build();
	}
}