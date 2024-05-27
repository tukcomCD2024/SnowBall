package com.example.memetory.domain.memes.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ZSetOperations;

@DisplayName("Ranking 서비스 테스트의 ")
@SpringBootTest
public class RankingServiceTest {
	private final String PREFIX = "LIKE_RANKING_DATE::";
	private final String POSTFIX_WEEK = "::WEEK";
	private final Long MEMES_ID = -1L;

	private String key;

	@Autowired
	private RankingService rankingService;
	@Autowired
	private ZSetOperations<String, Long> rankingZSet;
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@BeforeEach
	public void setUp() {
		key = PREFIX + LocalDate.now();
	}

	@AfterEach
	void clearRedis() {
		redisConnectionFactory.getConnection().flushAll();
	}

	@Test
	@DisplayName("존재하지 않는 memes와 memesId를 통한 score 증가 성공")
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

	@Test
	@DisplayName("존재하지 않는 주간 key를 통한 MemesIdList 반환")
	void Given_NotExistKey_When_findTopTenMemesLikeCountForWeek_Then_MemesIdList() {
		// given
		final int DAY = 7;

		for (int i = 0; i < DAY; i++) {
			LocalDate date = LocalDate.now().minusDays(i);
			String dateKey = PREFIX + date;
			rankingZSet.add(dateKey, (long)i, i);
		}

		// when
		List<Long> result = rankingService.findTopTenMemesLikeCountForWeek();

		//then
		assertThat(result.size()).isEqualTo(DAY - 1);
		assertThat(result.get(0)).isEqualTo(DAY - 1);
	}

	@Test
	@DisplayName("존재하는 주간 key를 통한 MemesIdList 반환")
	void Given_ExistKey_When_findTopTenMemesLikeCountForWeek_Then_MemesIdList() {
		// given
		key += POSTFIX_WEEK;
		rankingZSet.add(key, 7L, 7);

		for (int i = 0; i < 7; i++) {
			LocalDate date = LocalDate.now().minusDays(i);
			String dateKey = PREFIX + date;
			rankingZSet.add(dateKey, (long)i, i);
		}

		// when
		List<Long> result = rankingService.findTopTenMemesLikeCountForWeek();

		//then
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get(0)).isEqualTo(7L);
	}
}
