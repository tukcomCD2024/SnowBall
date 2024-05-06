package com.example.memetory.domain.meme;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.dto.MemeServiceDto;
import com.example.memetory.domain.meme.entity.Meme;

public class MemeFixture {
	public static Meme MEME(Member member) {
		return Meme.builder().s3Url("s3Url").member(member).build();
	}

	public static Meme SECOND_MEME(Member member) {
		return Meme.builder().s3Url("s3Url2").member(member).build();
	}

	public static Meme THIRD_MEME(Member member) {
		return Meme.builder().s3Url("s3Url3").member(member).build();
	}

	public static MemeServiceDto MEME_SERVICE_DTO() {
		return MemeServiceDto.builder()
			.memberId(-1L)
			.memeId(-1L)
			.s3Url("myS3Url")
			.build();
	}
}
