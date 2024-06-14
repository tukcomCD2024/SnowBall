package com.example.memetory.domain.comment.repository;

import static com.example.memetory.domain.comment.entity.QComment.*;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.memetory.domain.comment.dto.QCommentInfo;
import com.example.memetory.domain.member.dto.response.QMemberResponse;

@Component
public class CommentQDtoFactory {

	@Bean
	public QCommentInfo qCommentInfo() {
		return new QCommentInfo(comment.id,
			new QMemberResponse(comment.member.id, comment.member.nickname, comment.member.imageUrl, comment.member.createdAt),
			comment.content, comment.createdAt);
	}
}
