package com.example.memetory.domain.like.repository;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import com.example.memetory.domain.like.entity.Like;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.memes.dto.MemesRankDto;
import com.example.memetory.domain.memes.entity.Memes;

public interface LikeQueryRepository {
	Optional<Like> findLikeByMemberAndMemes(Member member, Memes memes);

	List<MemesRankDto> findDailyRankLimit100(LocalDate localDate);

	List<MemesRankDto> findWeeklyRankLimit100(Year year, int week);

	List<MemesRankDto> findMonthlyRankLimit100(YearMonth yearMonth);
}
