package com.example.memetory.domain.meme;

import static com.example.memetory.domain.member.MemberFixture.*;

import com.example.memetory.domain.meme.entity.Meme;

public class MemeFixture {
	public static Meme FIRST_MEME = Meme.builder()
		.s3Url("s3Url")
		.member(MEMBER())
		.build();

	public static Meme SECOND_MEME = Meme.builder()
		.s3Url("s3Url2")
		.member(MEMBER())
		.build();

	public static Meme THIRD_MEME = Meme.builder()
		.s3Url("s3Url3")
		.member(MEMBER())
		.build();

	public static Meme ANOTHER_MEMBER_MEME = Meme.builder()
		.s3Url("s3Url4")
		.member(SECOND_MEMBER())
		.build();

}
