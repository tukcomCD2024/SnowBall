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
		Duration ttl = Duration.ofDays(1);

		LocalDate now = LocalDate.now();
		increaseMemesLikeCountForToday(now, memesId, ttl);
		increaseMemesLikeCountForLastWeek(now, memesId, ttl);
		increaseMemesLikeCountForLastMonth(now, memesId, ttl);
	}

	private void increaseMemesLikeCountForToday(LocalDate now, Long memesId, Duration ttl) {
		String key = PREFIX + now;

		rankingZSet.incrementScore(key, memesId, 1);
		setExpireIfAbsent(key, ttl);
	}

	/**
	 * 주어진 key가 TTL을 가지고 있지 않을 경우 (즉, 새로 생성된 경우),
	 * 지정된 TTL을 부여한다.
	 *
	 * Redis의 TTL 값이 다음 중 하나일 경우 TTL을 설정함:
	 * - null: TTL 조회 실패 또는 일시적인 연결 문제
	 * - -1L: 무제한 저장 상태 (TTL이 없음)
	 */
	private void setExpireIfAbsent(String key, Duration ttl) {
		Long expire = redisTemplate.getExpire(key);
		if (expire == null || expire == -1L) {
			redisTemplate.expire(key, ttl);

		}
	}

	private void increaseMemesLikeCountForLastWeek(LocalDate now, Long memesId, Duration ttl) {
		WeekFields weekFields = WeekFields.of(Locale.KOREA);

		int year = now.getYear();
		int week = now.get(weekFields.weekOfYear());
		String key = PREFIX + String.format("%d-%02d", year, week) + POSTFIX_WEEK;

		rankingZSet.incrementScore(key, memesId, 1);

		setExpireIfAbsent(key, ttl);
	}

	private void increaseMemesLikeCountForLastMonth(LocalDate now, Long memesId, Duration ttl) {
		int year = now.getYear();
		int month = now.getMonthValue();
		String key = PREFIX + String.format("%d-%02d", year, month) + POSTFIX_MONTH;

		rankingZSet.incrementScore(key, memesId, 1);

		setExpireIfAbsent(key, ttl);
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

	public void decreaseCount(Long memesId) {
		String key = PREFIX + LocalDate.now();

		decreaseMemesLikeCountForToday(key, memesId);
		decreaseMemesLikeCountForLastWeek(key, memesId);
		decreaseMemesLikeCountForLastMonth(key, memesId);
	}

	private void decreaseMemesLikeCountForToday(String key, Long memesId) {
		rankingZSet.incrementScore(key, memesId, -1);
	}

	private void decreaseMemesLikeCountForLastWeek(String key, Long memesId) {
		key += POSTFIX_WEEK;

		unionMemesIfKeyNotExists(key, 7);
		rankingZSet.incrementScore(key, memesId, -1);
	}

	private void decreaseMemesLikeCountForLastMonth(String key, Long memesId) {
		key += POSTFIX_MONTH;

		unionMemesIfKeyNotExists(key, 30);
		rankingZSet.incrementScore(key, memesId, -1);
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
}
