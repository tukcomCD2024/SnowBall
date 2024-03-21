package com.example.memetory.domain.meme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.dto.AIServerSendDto;
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
}
