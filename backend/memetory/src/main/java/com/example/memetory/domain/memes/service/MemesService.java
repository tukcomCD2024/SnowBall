package com.example.memetory.domain.memes.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.example.memetory.domain.memes.exception.AccessDinedMemesException;
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

	@Transactional
	public MemesResponse registerMemes(MemesServiceDto memesServiceDto) {
		Meme meme = memeService.findMemeFromId(memesServiceDto.getMemeId());

		Member member = memberService.findMemberFromEmail(memesServiceDto.getEmail());
		Member memeMember = meme.getMember();
		memeService.certifyMemeMember(member, memeMember);

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
		certifyMemesMember(loginMember, memesMember);

		return memes;
	}

	private void certifyMemesMember(Member m1, Member m2) {
		if (!m1.equals(m2)) {
			throw new AccessDinedMemesException();
		}
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
	public List<MemesInfoResponse> findTopMemesByLike() {
		return memesRepository.findTopMemesOrderByLikeCount();
	}

	@Transactional(readOnly = true)
	public List<MemesInfoResponse> findTopMemesByLikeForMonth() {
		List<MemesRankDto> memesRankDtoList = rankingService.findTopTenMemesLikeCountForMonth();

		return convertMemesRankDtoListIntoMemesInfoResponseList(memesRankDtoList);
	}

	@Transactional(readOnly = true)
	public List<MemesInfoResponse> findTopMemesByLikeForWeek() {
		List<MemesRankDto> memesRankDtoList = rankingService.findTopTenMemesLikeCountForWeek();

		return convertMemesRankDtoListIntoMemesInfoResponseList(memesRankDtoList);
	}

	private List<MemesInfoResponse> convertMemesRankDtoListIntoMemesInfoResponseList(
		List<MemesRankDto> memesRankDtoList) {

		return memesRankDtoList.stream().map(this::convertMemesRankDtoInooMemesInfoResponse).toList();
	}

	private MemesInfoResponse convertMemesRankDtoInooMemesInfoResponse(MemesRankDto memesRank) {
		Memes memes = findMemesFromMemesId(memesRank.getMemesId());
		return MemesInfoResponse.fromMemesAndLikeCount(memes, memesRank.getScore());
	}
}
