package com.example.memetory.domain.memes.repository;

import static com.example.memetory.domain.memes.entity.QMemes.*;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.memetory.domain.memes.dto.response.QMemesInfo;

@Component
public class MemesQDtoFactory {

	@Bean
	public QMemesInfo qMemesInfo() {
		return new QMemesInfo(memes.id, memes.member.nickname, memes.title, memes.commentCount, memes.likeCount,
			memes.createdAt);
	}

	@Bean
	public QMemesInfo qMemesInfoSetLike() {
		return new QMemesInfo(memes.id, memes.member.nickname, memes.title, memes.commentCount,
			memes.count(), memes.createdAt);
	}
}
