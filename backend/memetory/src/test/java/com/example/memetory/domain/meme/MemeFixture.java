package com.example.memetory.domain.meme;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.entity.Meme;

public class MemeFixture {
	public static Meme FIRST_MEME(Member member) {
		return Meme.builder().s3Url("s3Url").member(member).build();
	}

	public static Meme SECOND_MEME(Member member) {
		return Meme.builder().s3Url("s3Url2").member(member).build();
	}

	public static Meme THIRD_MEME(Member member) {
		return Meme.builder().s3Url("s3Url3").member(member).build();
	}
}
