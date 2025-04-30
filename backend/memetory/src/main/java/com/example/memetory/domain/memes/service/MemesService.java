package com.example.memetory.domain.memes.service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.like.repository.LikeRepository;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.service.MemeService;
import com.example.memetory.domain.memes.dto.MemesRankDto;
import com.example.memetory.domain.memes.dto.MemesServiceDto;
import com.example.memetory.domain.memes.dto.response.MemesInfoResponse;
import com.example.memetory.domain.memes.dto.response.MemesInfoSliceResponse;
import com.example.memetory.domain.memes.dto.response.MemesResponse;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.event.DailyRankingCreatedEvent;
import com.example.memetory.domain.memes.event.MonthlyRankingCreatedEvent;
import com.example.memetory.domain.memes.event.WeeklyRankingCreatedEvent;
import com.example.memetory.domain.memes.exception.NotFoundMemesException;
import com.example.memetory.domain.memes.repository.MemesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemesService {
	private final MemberService memberService;
	private final MemeService memeService;
	private final MemesRepository memesRepository;
	private final RankingService rankingService;
	private final LikeRepository likeRepository;
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	public MemesResponse registerMemes(MemesServiceDto memesServiceDto) {
		Meme meme = memeService.findMemeFromId(memesServiceDto.getMemeId());

		Member member = memberService.findMemberFromEmail(memesServiceDto.getEmail());
		Member memeMember = meme.getMember();

		memberService.certifyMember(member, memeMember);

		Memes memes = memesServiceDto.toEntityFromMemberAndMeme(member, meme);
		Memes savedMemes = memesRepository.save(memes);

		return MemesResponse.of(savedMemes);
	}

	@Transactional
	public void deleteMemes(MemesServiceDto memesServiceDto) {
		Memes memes = findMemberMemes(memesServiceDto);

		memesRepository.delete(memes);
	}

	private Memes findMemberMemes(MemesServiceDto memesServiceDto) {
		Memes memes = findMemesFromMemesId(memesServiceDto.getMemesId());

		Member loginMember = memberService.findMemberFromEmail(memesServiceDto.getEmail());
		Member memesMember = memes.getMember();
		memberService.certifyMember(loginMember, memesMember);

		return memes;
	}

	@Transactional(readOnly = true)
	public MemesResponse findMemesResponse(MemesServiceDto memesServiceDto) {
		Memes memes = findMemesFromMemesId(memesServiceDto.getMemesId());
		return MemesResponse.of(memes);
	}

	@Transactional(readOnly = true)
	public Memes findMemesFromMemesId(Long memesId) {
		return memesRepository.findByMemesId(memesId).orElseThrow(NotFoundMemesException::new);
	}

	@Transactional(readOnly = true)
	public MemesInfoSliceResponse findMemesInfoSliceResponse(Pageable pageable) {
		Slice<MemesInfoResponse> memesSlice = memesRepository.findMemesInfoSlice(pageable);

		return MemesInfoSliceResponse.builder()
			.currentPage(pageable.getPageNumber())
			.hasNext(memesSlice.hasNext())
			.memesInfoResponseList(memesSlice.getContent())
			.build();
	}

	@Transactional(readOnly = true)
	public List<MemesInfoResponse> findDailyTop10Memes(LocalDate localDate) {
		return findTop10MemesFromRanking(() -> rankingService.findDailyRanking(localDate),
			() -> LocalDate.now().equals(localDate), () -> {
				List<MemesRankDto> dtos = likeRepository.findDailyRankLimit100(localDate);
				eventPublisher.publishEvent(new DailyRankingCreatedEvent(localDate, dtos));
				return dtos;
			});
	}

	@Transactional(readOnly = true)
	public List<MemesInfoResponse> findWeeklyTop10Memes(Year year, int week) {
		WeekFields weekFields = WeekFields.of(Locale.KOREA);
		LocalDate today = LocalDate.now();

		return findTop10MemesFromRanking(() -> rankingService.findWeeklyRank(year, week),
			() -> year.getValue() == today.getYear() && week == today.get(weekFields.weekOfYear()), () -> {
				List<MemesRankDto> dtos = likeRepository.findWeeklyRankLimit100(year, week);
				eventPublisher.publishEvent(new WeeklyRankingCreatedEvent(year, week, dtos));
				return dtos;
			});
	}

	@Transactional(readOnly = true)
	public List<MemesInfoResponse> findMonthlyTop10Memes(YearMonth yearMonth) {
		return findTop10MemesFromRanking(() -> rankingService.findMonthlyRank(yearMonth),
			() -> YearMonth.now().equals(yearMonth), () -> {
				List<MemesRankDto> dtos = likeRepository.findMonthlyRankLimit100(yearMonth);
				eventPublisher.publishEvent(new MonthlyRankingCreatedEvent(yearMonth, dtos));
				return dtos;
			});
	}

	private List<MemesInfoResponse> findTop10MemesFromRanking(
		Supplier<List<MemesRankDto>> cacheSupplier,
		BooleanSupplier isCurrentPeriodChecker,
		Supplier<List<MemesRankDto>> dbSupplierWithEvent) {

		List<MemesRankDto> rankList = cacheSupplier.get();

		if (rankList.isEmpty()) {
			if (isCurrentPeriodChecker.getAsBoolean()) {
				return List.of();
			}
			rankList = dbSupplierWithEvent.get();
		}

		Map<Long, MemesRankDto> rankMap = rankList.stream()
			.collect(Collectors.toMap(MemesRankDto::getMemesId, Function.identity()));

		List<Memes> memes = memesRepository.findMemesByIdIn(rankMap.keySet());

		return memes.stream().map(m -> {
			MemesRankDto dto = rankMap.get(m.getId());
			return MemesInfoResponse.fromMemesAndLikeCount(m, dto.getScore());
		}).sorted(Comparator.comparing(MemesInfoResponse::getLikeCount).reversed()).limit(10).toList();
	}
}
