package com.example.memetory.domain.meme.repository;

import static com.example.memetory.domain.meme.entity.QMeme.*;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.memetory.domain.meme.dto.QMemeResponse;

@Component
public class MemeQDtoFactory {

	@Bean
	public QMemeResponse qMemeResponse() {
		return new QMemeResponse(
			meme.id,
			meme.s3Url,
			meme.createdAt,
			meme.updatedAt);
	}
}
