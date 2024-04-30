package com.example.memetory.domain.meme.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.dto.AIServerSendDto;
import com.example.memetory.domain.meme.dto.MemePageResponse;
import com.example.memetory.domain.meme.dto.MemeResponse;
import com.example.memetory.domain.meme.dto.MemeServiceDto;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.exception.NotFoundMemeException;
import com.example.memetory.domain.meme.repository.MemeRepository;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemeService {
	private final MemberService memberService;
	private final MemeRepository memeRepository;

	@Transactional
	public MemeResponse register(MemeServiceDto memeServiceDto) {
		Member member = memberService.findById(memeServiceDto.getMemberId());
		Meme meme = memeServiceDto.toEntity(member);

		return MemeResponse.of(memeRepository.save(meme));
	}

	@Transactional(readOnly = true)
	public String getAIServerSendJson(MemeServiceDto memeServiceDto) {
		Gson gson = new Gson();

		Member member = memberService.findByEmail(memeServiceDto.getEmail());

		AIServerSendDto aiServerSendDto = AIServerSendDto.builder()
				.memberId(member.getId())
				.scene(memeServiceDto.getScene())
				.build();

		return gson.toJson(aiServerSendDto);
	}

	@Transactional(readOnly = true)
	public MemeResponse getMeme(MemeServiceDto memeServiceDto) {
		Meme meme = memeRepository.findById(memeServiceDto.getMemeId()).orElseThrow(NotFoundMemeException::new);

		return MemeResponse.of(meme);
	}

	@Transactional(readOnly = true)
	public boolean checkMember(MemeServiceDto memeServiceDto) {
		Meme meme = memeRepository.findById(memeServiceDto.getMemeId()).orElseThrow(NotFoundMemeException::new);
		Member member = memberService.findById(memeServiceDto.getMemberId());

		return meme.getMember() != member;
	}

	@Transactional
	public MemePageResponse getAllMeme(MemeServiceDto memeServiceDto, Pageable pageable) {
		Member member = memberService.findByEmail(memeServiceDto.getEmail());

		Page<MemeResponse> memeList = memeRepository.findAllByMember(member, pageable);

		return MemePageResponse.builder()
			.totalPage(memeList.getTotalPages())
			.currentPage(pageable.getPageNumber())
				.memeList(memeList.getContent())
				.build();
	}

	// Service 계층 끼리의 밈 조회
	@Transactional(readOnly = true)
	public Meme getMemeBetweenService(Long memeId) {
		return memeRepository.findById(memeId).orElseThrow(NotFoundMemeException::new);
	}
}
