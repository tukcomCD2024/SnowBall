package com.example.memetory.domain.memes;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.memes.entity.Memes;

public class MemesFixture {
	public static Memes MEMES(Member member, Meme meme){
		return Memes.builder()
			.title("new Memes")
			.member(member)
			.meme(meme)
			.likeCount(0L)
			.commentCount(0L)
			.build();
	}
}
