package com.example.memetory.domain.memes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.memes.dto.MemesRankDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RankingService {
	private final String PREFIX = "LIKE_RANKING_DATE::";
	private final String POSTFIX_WEEK = "::WEEK";
	private final String POSTFIX_MONTH = "::MONTH";
	private final Long TOP_TEN = 9L;

	private final ZSetOperations<String, Long> rankingZSet;

	@Transactional
	public void increaseTodayMemesLikeCountFromMemesId(Long memesId) {
		String key = PREFIX + LocalDate.now();

		increaseMemesLikeCountForToday(key, memesId);
		increaseMemesLikeCountForLastWeek(key, memesId);
		increaseMemesLikeCountForLastMonth(key, memesId);
	}

	private void increaseMemesLikeCountForToday(String key, Long memesId) {
		rankingZSet.incrementScore(key, memesId, 1);
	}

	private void increaseMemesLikeCountForLastWeek(String key, Long memesId) {
		key += POSTFIX_WEEK;

		unionMemesIfKeyNotExists(key, 7);
		rankingZSet.incrementScore(key, memesId, 1);
	}

	private void increaseMemesLikeCountForLastMonth(String key, Long memesId) {
		key += POSTFIX_MONTH;

		unionMemesIfKeyNotExists(key, 30);
		rankingZSet.incrementScore(key, memesId, 1);
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

	@Transactional
	public void decreaseTodayMemesLikeCountFromMemesId(Long memesId) {
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

	@Transactional(readOnly = true)
	public List<MemesRankDto> findTopTenMemesLikeCountForWeek() {
		String key = PREFIX + LocalDate.now() + POSTFIX_WEEK;

		unionMemesIfKeyNotExists(key, 7);

		Set<ZSetOperations.TypedTuple<Long>> rankTuple = rankingZSet.reverseRangeWithScores(key, 0, TOP_TEN);
		List<MemesRankDto> result = rankTuple.stream().map(MemesRankDto::of).toList();

		return result;
	}

	@Transactional(readOnly = true)
	public List<MemesRankDto> findTopTenMemesLikeCountForMonth() {
		String key = PREFIX + LocalDate.now() + POSTFIX_MONTH;

		unionMemesIfKeyNotExists(key, 30);

		Set<ZSetOperations.TypedTuple<Long>> rankTuple = rankingZSet.reverseRangeWithScores(key, 0, TOP_TEN);
		List<MemesRankDto> result = rankTuple.stream().map(MemesRankDto::of).toList();

		return result;
	}
}
