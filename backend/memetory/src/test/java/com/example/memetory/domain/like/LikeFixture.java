package com.example.memetory.domain.like;

import com.example.memetory.domain.like.dto.LikeServiceDto;
import com.example.memetory.domain.like.entity.Like;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.memes.entity.Memes;

public class LikeFixture {
	public static Like LIKE(Member member, Memes memes) {
		return Like.builder()
			.member(member)
			.memes(memes)
			.build();
	}

	public static LikeServiceDto LIKE_SERVICE_DTO() {
		return LikeServiceDto.builder()
			.memesId(-1L)
			.email("junrain@ourservice.com")
			.build();
	}
}
