package com.example.memetory.domain.like.repository;

import static com.example.memetory.domain.like.entity.QLike.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.memetory.domain.like.entity.Like;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.memes.dto.MemesRankDto;
import com.example.memetory.domain.memes.entity.Memes;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LikeQueryRepositoryImpl implements LikeQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<Like> findLikeByMemberAndMemes(Member member, Memes memes) {
		return Optional.ofNullable(
			jpaQueryFactory.selectFrom(like).where(like.member.eq(member).and(like.memes.eq(memes))).fetchFirst());
	}

	@Override
	public List<MemesRankDto> findDailyRankLimit100(LocalDate localDate) {
		return jpaQueryFactory.select(Projections.constructor(MemesRankDto.class, like.memes.id, like.id.count()))
			.from(like)
			.where(like.createdAt.goe(localDate.atStartOfDay()),
				like.createdAt.lt(localDate.plusDays(1).atStartOfDay()))
			.groupBy(like.memes)
			.orderBy(like.id.count().desc())
			.limit(100L)
			.fetch();
	}

	@Override
	public List<MemesRankDto> findWeeklyRankLimit100(Year year, int week) {
		// 주차 기준: 한국 (월요일 시작)
		WeekFields weekFields = WeekFields.of(Locale.KOREA);

		// 해당 주의 시작 날짜 (월요일)
		LocalDate startOfWeek = year.atDay(1).with(weekFields.weekOfYear(), week).with(weekFields.dayOfWeek(), 1);

		// 다음 주의 시작 날짜 (exclusive)
		LocalDate endOfWeek = startOfWeek.plusDays(7);

		LocalDateTime startDateTime = startOfWeek.atStartOfDay();     // 월요일 00:00
		LocalDateTime endDateTime = endOfWeek.atStartOfDay();         // 다음 주 월요일 00:00

		return jpaQueryFactory.select(Projections.constructor(MemesRankDto.class, like.memes.id, like.id.count()))
			.from(like)
			.where(like.createdAt.goe(startDateTime), like.createdAt.lt(endDateTime))
			.groupBy(like.memes.id)
			.orderBy(like.id.count().desc())
			.limit(100)
			.fetch();
	}

	@Override
	public List<MemesRankDto> findMonthlyRankLimit100(YearMonth yearMonth) {
		// 해당 월의 시작일 00:00
		LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();

		// 다음 달의 시작일 00:00 (해당 월의 마지막까지 포함되도록)
		LocalDateTime startOfNextMonth = yearMonth.plusMonths(1).atDay(1).atStartOfDay();

		return jpaQueryFactory.select(Projections.constructor(MemesRankDto.class, like.memes.id, like.id.count()))
			.from(like)
			.where(like.createdAt.goe(startOfMonth), like.createdAt.lt(startOfNextMonth))
			.groupBy(like.memes.id)
			.orderBy(like.id.count().desc())
			.limit(100)
			.fetch();
	}

}
