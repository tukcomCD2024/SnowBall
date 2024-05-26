package com.example.memetory.domain.memes.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;

import jakarta.transaction.Transactional;

@DisplayName("Ranking 서비스 테스트의 ")
@SpringBootTest
@Transactional
public class RankingServiceTest {
	private static final String PREFIX = "LIKE_RANKING_DATE::";
	static String key;

	private final Long MEMES_ID = -1L;

	@Autowired
	private RankingService rankingService;
	@Autowired
	private ZSetOperations<String, Long> rankingZSet;

	@BeforeAll
	public static void setUp() {
		key = PREFIX + LocalDate.now();
	}

	@AfterEach
	void clearRedis() {
		rankingZSet.remove(key, MEMES_ID);
	}

	@Test
	@DisplayName("존재하지 않는 memes와 memesId를 통한 score 증가 성송")
	void Given_memesId_When_increaseTodayMemesLikeCountFromMemesId_Then_Memems_Score() {
		// given
		Double expectedScore = 1.0;

		// when
		rankingService.increaseTodayMemesLikeCountFromMemesId(MEMES_ID);

		// then
		assertThat(rankingZSet.score(key, MEMES_ID)).isEqualTo(expectedScore);
	}

	@Test
	@DisplayName("존재하는 memes와 memesId를 통한 score 증가 성공")
	void Given_existMemesAndMemesId_When_increaseTodayMemesLikeCountFromMemesId_Then_Memes_Score() {
		// given
		Double expectedScore = 2.0;

		rankingZSet.add(key, MEMES_ID, 1);

		// when
		rankingService.increaseTodayMemesLikeCountFromMemesId(MEMES_ID);

		// then
		assertThat(rankingZSet.score(key, MEMES_ID)).isEqualTo(expectedScore);
	}

	@Test
	@DisplayName("존재하는 memes와 memesId를 통한 score 증가")
	void Given_existMemesAndMemesId_When_decreaseTodayMemesLikeCountFromMemesId_Then_Memes_Score() {
		// given
		Double expectedScore = 1.0;

		rankingZSet.add(key, MEMES_ID, 2);

		// when
		rankingService.decreaseTodayMemesLikeCountFromMemesId(MEMES_ID);

		// then
		assertThat(rankingZSet.score(key, MEMES_ID)).isEqualTo(expectedScore);
	}

}
