package com.example.memetory.domain.memes.repository;

import static com.example.memetory.domain.memes.entity.QMemes.*;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.memetory.domain.memes.dto.response.QMemesInfoResponse;

@Component
public class MemesQDtoFactory {

	@Bean
	public QMemesInfoResponse qMemesInfo() {
		return new QMemesInfoResponse(memes.id, memes.member.nickname, memes.title, memes.commentCount, memes.likeCount,
			memes.createdAt);
	}

	@Bean
	public QMemesInfoResponse qMemesInfoSetLike() {
		return new QMemesInfoResponse(memes.id, memes.member.nickname, memes.title, memes.commentCount,
			memes.count(), memes.createdAt);
	}
}
