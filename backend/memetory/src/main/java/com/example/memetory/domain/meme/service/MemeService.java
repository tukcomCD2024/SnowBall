package com.example.memetory.domain.meme.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.dto.AIServerSendDto;
import com.example.memetory.domain.meme.dto.MemeListResponse;
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
	public void register(MemeServiceDto memeServiceDto) {
		Member member = memberService.findById(memeServiceDto.getMemberId());
		Meme meme = memeServiceDto.toEntity(member);

		memeRepository.save(meme);
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
	public MemeListResponse getAllMeme(MemeServiceDto memeServiceDto) {
		Member member = memberService.findByEmail(memeServiceDto.getEmail());

		List<MemeResponse> memeList = memeRepository.findAllByMember(member)
				.stream()
				.map(MemeResponse::of)
				.toList();

		return MemeListResponse.builder()
				.memeList(memeList)
				.build();
	}

	// Service 계층 끼리의 밈 조회
	@Transactional(readOnly = true)
	public Meme getMemeBetweenService(Long memeId) {
		Meme foundMeme = memeRepository.findById(memeId).orElseThrow(NotFoundMemeException::new);
		return foundMeme;
	}
}
