package com.example.memetory.domain.memes.service;

import java.time.LocalDate;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RankingService {
	private final String PREFIX = "LIKE_RANKING_DATE::";
	private final ZSetOperations<String, Long> rankingZSet;

	@Transactional
	public void increaseTodayMemesLikeCountFromMemesId(Long memesId) {
		String key = PREFIX + LocalDate.now();

		if (isExistedTodayMemesId(key, memesId)) {
			rankingZSet.incrementScore(key, memesId, 1);
			return;
		}
		saveTodayMemesLikeCount(key, memesId);
	}

	private boolean isExistedTodayMemesId(String key, Long memesId) {
		Double score = rankingZSet.score(key, memesId);

		if (score == null) {
			return false;
		}
		return true;
	}

	private void saveTodayMemesLikeCount(String key, Long memesId) {
		rankingZSet.add(key, memesId, 1);
	}

	@Transactional
	public void decreaseTodayMemesLikeCountFromMemesId(Long memesId) {
		String key = PREFIX + LocalDate.now();

		rankingZSet.incrementScore(key, memesId, -1);
	}

	// 현재 - 일주일 전(Key) 랭킹을 구함

	// 현재 - 한달 전(Key) 랭킹을 구함
}
