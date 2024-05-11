package com.example.memetory.domain.memes.service;

import static java.time.LocalDateTime.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.service.MemeService;
import com.example.memetory.domain.memes.dto.MemesServiceDto;
import com.example.memetory.domain.memes.dto.response.MemesInfo;
import com.example.memetory.domain.memes.dto.response.MemesInfoSliceResponse;
import com.example.memetory.domain.memes.dto.response.MemesResponse;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.exception.NotDeleteMemesException;
import com.example.memetory.domain.memes.exception.NotFoundMemesException;
import com.example.memetory.domain.memes.repository.MemesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemesService {
	private final MemberService memberService;
	private final MemeService memeService;
	private final MemesRepository memesRepository;

	@Transactional
	public void register(MemesServiceDto memesServiceDto) {
		Member member = memberService.findByEmail(memesServiceDto.getEmail());
		Meme meme = memeService.getMemeBetweenService(memesServiceDto.getMemeId());

		Memes newMemes = memesServiceDto.toEntity(member, meme);
		memesRepository.save(newMemes);
	}

	@Transactional
	public void delete(MemesServiceDto memesServiceDto) {
		Memes memes = findById(memesServiceDto.getMemesId());
		if (memes.getMember() != memberService.findByEmail(memesServiceDto.getEmail())) {
			throw new NotDeleteMemesException();
		}
		memesRepository.delete(memes);
	}

	@Transactional(readOnly = true)
	public MemesResponse getMemesResponse(MemesServiceDto memesServiceDto) {
		return MemesResponse.of(findById(memesServiceDto.getMemeId()));
	}

	@Transactional(readOnly = true)
	public MemesInfoSliceResponse getMemesInfoSliceResponse(Pageable pageable) {
		Slice<MemesInfo> memesSlice = memesRepository.findAllMemesSlice(pageable);

		return MemesInfoSliceResponse.builder()
			.currentPage(pageable.getPageNumber())
			.hasNext(memesSlice.hasNext())
			.memesInfoList(memesSlice.getContent())
			.build();
	}

	@Transactional(readOnly = true)
	public List<MemesInfo> getTopMemesByLike() {
		return memesRepository.findTopMemesOrderByLikeCount();
	}

	@Transactional(readOnly = true)
	public List<MemesInfo> findTopMemesByLikeForMonth() {
		return memesRepository.findTopMemesByLikeCountForPeriod(now().minusMonths(1));
	}

	@Transactional(readOnly = true)
	public List<MemesInfo> findTopMemesByLikeForWeek() {
		return memesRepository.findTopMemesByLikeCountForPeriod(now().minusWeeks(1));
	}

	@Transactional(readOnly = true)
	public Memes getMemesBetweenService(Long memesId) {
		return findById(memesId);
	}

	private Memes findById(Long memesId) {
		return memesRepository.findByMemesId(memesId).orElseThrow(NotFoundMemesException::new);
	}
}
