package com.example.memetory.domain.memes.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.DefaultTypedTuple;
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
	private final Long LIMIT = 99L;

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

	// 그냥 Redis에서 100개의 데이터를 반환
	public List<MemesRankDto> findDailyRanking(LocalDate localDate) {
		String key = buildDailyKey(localDate);

		Set<ZSetOperations.TypedTuple<Long>> rankTuple = rankingZSet.reverseRangeWithScores(key, 0, LIMIT);

		return rankTuple.stream().map(MemesRankDto::of).toList();
	}

	public List<MemesRankDto> findWeeklyRank(Year year, int week) {
		String key = PREFIX + String.format("%d-%02d", year.getValue(), week) + POSTFIX_WEEK;

		Set<ZSetOperations.TypedTuple<Long>> rankTuple = rankingZSet.reverseRangeWithScores(key, 0, LIMIT);

		return rankTuple.stream().map(MemesRankDto::of).toList();
	}

	public List<MemesRankDto> findMonthlyRank(YearMonth yearMonth) {
		String key = PREFIX + yearMonth.toString() + POSTFIX_MONTH; // e.g. memes:2024-04:MONTH

		Set<ZSetOperations.TypedTuple<Long>> rankTuple = rankingZSet.reverseRangeWithScores(key, 0, LIMIT);

		return rankTuple.stream()
			.map(MemesRankDto::of)
			.toList();
	}

	public void createDailyRanking(LocalDate date, List<MemesRankDto> rankDtoList) {
		String key = buildDailyKey(date);

		Set<ZSetOperations.TypedTuple<Long>> tuples = rankDtoList.stream()
			.map(dto -> new DefaultTypedTuple<>(dto.getMemesId(), (double)dto.getScore()))
			.collect(Collectors.toSet());

		rankingZSet.add(key, tuples);
		redisTemplate.expire(key, Duration.ofDays(30));
	}

	public void createWeeklyRanking(Year year, int week, List<MemesRankDto> memesRankDtos) {
		String key = PREFIX + String.format("%d-%02d", year.getValue(), week) + POSTFIX_WEEK;

		Set<ZSetOperations.TypedTuple<Long>> tuples = memesRankDtos.stream()
			.map(dto -> new DefaultTypedTuple<>(dto.getMemesId(), (double) dto.getScore()))
			.collect(Collectors.toSet());

		rankingZSet.add(key, tuples);
		redisTemplate.expire(key, Duration.ofDays(30));
	}

	public void createMonthlyRanking(YearMonth yearMonth, List<MemesRankDto> memesRankDtos) {
		String key = PREFIX + yearMonth.toString() + POSTFIX_MONTH; // e.g. memes:2024-04:MONTH

		Set<ZSetOperations.TypedTuple<Long>> tuples = memesRankDtos.stream()
			.map(dto -> new DefaultTypedTuple<>(dto.getMemesId(), (double) dto.getScore()))
			.collect(Collectors.toSet());

		rankingZSet.add(key, tuples);
		redisTemplate.expire(key, Duration.ofDays(365));
	}

}


