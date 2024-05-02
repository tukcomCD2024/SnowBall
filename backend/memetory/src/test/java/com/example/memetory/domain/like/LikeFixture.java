package com.example.memetory.domain.like;

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
}
