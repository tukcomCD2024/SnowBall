package com.example.memetory.domain.comment;

import com.example.memetory.domain.comment.dto.request.CommentRequest;
import com.example.memetory.domain.comment.entity.Comment;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.memes.entity.Memes;

public class CommentFixture {
	public static Comment COMMENT(Member member, Memes memes) {
		return Comment.builder()
			.member(member)
			.memes(memes)
			.content("댓글")
			.build();
	}

	public static CommentRequest COMMENT_REQUEST() {
		return new CommentRequest("댓글", -1L, "junRain@ourservce.com");
	}
}
