package com.example.memetory.domain.memes.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.memetory.domain.memes.service.RankingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RankingEventListener {
	private final RankingService rankingService;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void incrementCount(LikeCreatedEvent event) {
		try {
			rankingService.increaseCount(event.memesId());
		} catch (Exception e) {
			log.warn("Redis 업데이트 실패 (트랜잭션 커밋 이후): {}", event.memesId(), e);
		}
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void decrementCount(LikeDeletedEvent event) {
		try {
			rankingService.decreaseCount(event.memesId());
		} catch (Exception e) {
			log.warn("Redis 업데이트 실패 (트랜잭션 커밋 이후): {}", event.memesId(), e);
		}
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void createRanking(DailyRankingCreatedEvent event) {
		try {
			rankingService.createDailyRanking(event.date(), event.memesRank());
		} catch (Exception e) {
			log.warn("Redis 업데이트 실패 (트랜잭션 커밋 이후)");
		}
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void createRanking(WeeklyRankingCreatedEvent event) {
		try {
			rankingService.createWeeklyRanking(event.year(), event.week(), event.memesRank());
		} catch (Exception e) {
			log.warn("Redis 업데이트 실패 (트랜잭션 커밋 이후)");
		}
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void createRanking(MonthlyRankingCreatedEvent event) {
		try {
			rankingService.createMonthlyRanking(event.yearMonth(), event.memesRankDtoList());
		} catch (Exception e) {
			log.warn("Redis 업데이트 실패 (트랜잭션 커밋 이후)");
		}
	}
}
