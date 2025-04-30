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

import com.example.memetory.domain.memes.dto.MemesRankDto;

@DisplayName("Ranking 서비스 테스트의 ")
@SpringBootTest
public class RankingServiceTest {
	private final String PREFIX = "LIKE_RANKING_DATE::";
	private final String POSTFIX_WEEK = "::WEEK";
	private final String POSTFIX_MONTH = "::MONTH";
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
		redisConnectionFactory.getConnection().flushAll();
		key = PREFIX + LocalDate.now();
	}

	@AfterEach
	void clearRedis() {
		redisConnectionFactory.getConnection().flushAll();
	}

	@Test
	@DisplayName("존재하지 않는 memes와 memesId를 통한 score 증가 성공")
	void Given_memesId_When_increaseTodayMemesLikeCountFromMemesId_Then_Memes_Score() {
		// given
		Double expectedScore = 1.0;

		// when
		rankingService.increaseCount(MEMES_ID);

		// then
		assertThat(rankingZSet.score(key, MEMES_ID)).isEqualTo(expectedScore);
		assertThat(rankingZSet.score(key + POSTFIX_WEEK, MEMES_ID)).isEqualTo(expectedScore);
		assertThat(rankingZSet.score(key + POSTFIX_MONTH, MEMES_ID)).isEqualTo(expectedScore);
	}

	@Test
	@DisplayName("존재하는 memes와 memesId를 통한 score 증가 성공")
	void Given_existMemesAndMemesId_When_increaseTodayMemesLikeCountFromMemesId_Then_Memes_Score() {
		// given
		Double expectedScore = 2.0;

		// when
		rankingService.increaseCount(MEMES_ID);
		rankingService.increaseCount(MEMES_ID);

		// then
		assertThat(rankingZSet.score(key, MEMES_ID)).isEqualTo(expectedScore);
		assertThat(rankingZSet.score(key + POSTFIX_WEEK, MEMES_ID)).isEqualTo(expectedScore);
		assertThat(rankingZSet.score(key + POSTFIX_MONTH, MEMES_ID)).isEqualTo(expectedScore);
	}

	@Test
	@DisplayName("존재하는 memes와 memesId를 통한 score 감소")
	void Given_existMemesAndMemesId_When_decreaseTodayMemesLikeCountFromMemesId_Then_Memes_Score() {
		// given
		Double expectedScore = -1.0;

		// when
		rankingService.decreaseCount(MEMES_ID);

		// then
		assertThat(rankingZSet.score(key, MEMES_ID)).isEqualTo(expectedScore);
		assertThat(rankingZSet.score(key + POSTFIX_WEEK, MEMES_ID)).isEqualTo(expectedScore);
		assertThat(rankingZSet.score(key + POSTFIX_MONTH, MEMES_ID)).isEqualTo(expectedScore);
	}

	@Test
	@DisplayName("존재하지 않는 주간 key를 통한 MemesIdList 반환")
	void Given_NotExistKey_When_findDailyRankingTenMemesLikeCountForWeek_Then_MemesIdList() {
		// given
		for (int i = 0; i < 7; i++) {
			LocalDate date = LocalDate.now().minusDays(i);
			String dateKey = PREFIX + date;
			rankingZSet.add(dateKey, (long)i, i);
		}

		// when
		List<MemesRankDto> result = rankingService.findDailyRanking();

		//then
		assertThat(result.size()).isEqualTo(6);
		assertThat(result.get(0).getMemesId()).isEqualTo(6);
	}

	@Test
	@DisplayName("존재하는 주간 key를 통한 MemesIdList 반환")
	void Given_ExistKey_When_findDailyRankingTenMemesLikeCountForWeek_Then_MemesIdList() {
		// given
		key += POSTFIX_WEEK;
		rankingZSet.add(key, 7L, 7);

		for (int i = 0; i < 7; i++) {
			LocalDate date = LocalDate.now().minusDays(i);
			String dateKey = PREFIX + date;
			rankingZSet.add(dateKey, (long)i, i);
		}

		// when
		List<MemesRankDto> result = rankingService.findDailyRanking();

		//then
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get(0).getMemesId()).isEqualTo(7L);
	}
}
