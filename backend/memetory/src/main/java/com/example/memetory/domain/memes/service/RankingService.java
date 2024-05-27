package com.example.memetory.domain.memes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RankingService {
	private final String PREFIX = "LIKE_RANKING_DATE::";
	private final String POSTFIX_WEEK = "::WEEK";
	private final String POSTFIX_MONTH = "::MONTH";
	private final Long TOP_TEN = 10L;

	private final ZSetOperations<String, Long> rankingZSet;

	@Transactional
	public void increaseTodayMemesLikeCountFromMemesId(Long memesId) {
		String key = PREFIX + LocalDate.now();

		rankingZSet.incrementScore(key, memesId, 1);
	}

	// Todo 음수를 확인하는 로직 추가 필요해 보임
	@Transactional
	public void decreaseTodayMemesLikeCountFromMemesId(Long memesId) {
		String key = PREFIX + LocalDate.now();

		rankingZSet.incrementScore(key, memesId, -1);
	}

	@Transactional(readOnly = true)
	public List<Long> findTopTenMemesLikeCountForWeek() {
		String key = PREFIX + LocalDate.now() + POSTFIX_WEEK;

		if (isNotExistedKey(key))
			unionMemesFromKeyAndDay(key, 7);

		Set<Long> result = rankingZSet.reverseRange(key, 0, TOP_TEN);
		return List.copyOf(result);
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

	@Transactional(readOnly = true)
	public List<Long> findTopTenMemesLikeCountForMonth() {
		String key = PREFIX + LocalDate.now() + POSTFIX_MONTH;

		if (isNotExistedKey(key)) {
			unionMemesFromKeyAndDay(key, 30);
		}

		Set<Long> result = rankingZSet.reverseRange(key, 0, TOP_TEN);
		return List.copyOf(result);
	}
}
