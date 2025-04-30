package com.example.memetory.domain.memes.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.example.memetory.domain.memes.dto.MemesRankDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RankingService {
	private final String PREFIX = "LIKE_RANKING_DATE::";
	private final String POSTFIX_WEEK = "::WEEK";
	private final String POSTFIX_MONTH = "::MONTH";
	private final Long TOP_TEN = 9L;

	@Qualifier("rankingRedisTemplate")
	private final RedisTemplate<String, Long> redisTemplate;
	private final ZSetOperations<String, Long> rankingZSet;

	public void increaseCount(Long memesId) {
		LocalDate now = LocalDate.now();

		increaseWithTtl(buildDailyKey(now), memesId);
		increaseWithTtl(buildWeeklyKey(now), memesId);
		increaseWithTtl(buildMonthlyKey(now), memesId);
	}

	private void increaseWithTtl(String key, Long memesId) {
		rankingZSet.incrementScore(key, memesId, 1);
		setExpireIfAbsent(key, Duration.ofDays(1));
	}

	private void setExpireIfAbsent(String key, Duration ttl) {
		Long expire = redisTemplate.getExpire(key);
		if (expire == null || expire == -1L) {
			redisTemplate.expire(key, ttl);
		}
	}

	private String buildDailyKey(LocalDate date) {
		return PREFIX + date;
	}

	private String buildWeeklyKey(LocalDate date) {
		WeekFields weekFields = WeekFields.of(Locale.KOREA);
		int year = date.getYear();
		int week = date.get(weekFields.weekOfYear());
		return PREFIX + String.format("%d-%02d", year, week) + POSTFIX_WEEK;
	}

	private String buildMonthlyKey(LocalDate date) {
		int year = date.getYear();
		int month = date.getMonthValue();
		return PREFIX + String.format("%d-%02d", year, month) + POSTFIX_MONTH;
	}

	public void decreaseCount(Long memesId) {
		LocalDate now = LocalDate.now();

		decreaseIfExists(buildDailyKey(now), memesId);
		decreaseIfExists(buildWeeklyKey(now), memesId);
		decreaseIfExists(buildMonthlyKey(now), memesId);
	}

	private void decreaseIfExists(String key, Long memesId) {
		if (isMemberScored(key, memesId)) {
			rankingZSet.incrementScore(key, memesId, -1);
		}
	}

	private boolean isMemberScored(String key, Long member) {
		Double score = rankingZSet.score(key, member);
		return score != null && score > 0;
	}

	public List<MemesRankDto> findTopTenMemesLikeCountForWeek() {
		String key = PREFIX + LocalDate.now() + POSTFIX_WEEK;

		unionMemesIfKeyNotExists(key, 7);

		Set<ZSetOperations.TypedTuple<Long>> rankTuple = rankingZSet.reverseRangeWithScores(key, 0, TOP_TEN);
		List<MemesRankDto> result = rankTuple.stream().map(MemesRankDto::of).toList();

		return result;
	}

	public List<MemesRankDto> findTopTenMemesLikeCountForMonth() {
		String key = PREFIX + LocalDate.now() + POSTFIX_MONTH;

		unionMemesIfKeyNotExists(key, 30);

		Set<ZSetOperations.TypedTuple<Long>> rankTuple = rankingZSet.reverseRangeWithScores(key, 0, TOP_TEN);
		List<MemesRankDto> result = rankTuple.stream().map(MemesRankDto::of).toList();

		return result;
	}

	private void unionMemesIfKeyNotExists(String key, int day) {
		if (isNotExistedKey(key)) {
			unionMemesFromKeyAndDay(key, day);
		}
	}

	private boolean isNotExistedKey(String key) {
		Set<Long> check = rankingZSet.range(key, 0, 1);

		return check.isEmpty();
	}

	private void unionMemesFromKeyAndDay(String key, int day) {
		List<String> keyList = new ArrayList<>();
		LocalDate today = LocalDate.now();

		for (int i = 1; i < day; i++) {
			LocalDate date = today.minusDays(i);
			keyList.add(PREFIX + date);
		}

		rankingZSet.unionAndStore(key, keyList, key);
	}
}


