package com.example.memetory.domain.meme.service;

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
import com.example.memetory.domain.meme.exception.AccessDeniedMemeException;
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
	public MemeResponse registerMeme(MemeServiceDto memeServiceDto) {
		Member member = memberService.findMemberFromId(memeServiceDto.getMemberId());
		Meme meme = memeServiceDto.toEntityFromMember(member);

		Meme savedMeme = memeRepository.save(meme);

		return MemeResponse.of(savedMeme);
	}

	@Transactional(readOnly = true)
	public String getAIServerSendJson(MemeServiceDto memeServiceDto) {
		Gson gson = new Gson();

		Member member = memberService.findMemberFromEmail(memeServiceDto.getEmail());

		AIServerSendDto aiServerSendDto = AIServerSendDto.builder()
			.memberId(member.getId())
			.scene(memeServiceDto.getScene())
			.build();

		return gson.toJson(aiServerSendDto);
	}

	@Transactional(readOnly = true)
	public MemeResponse findMemberMemeResponse(MemeServiceDto memeServiceDto) {
		Member member = memberService.findMemberFromId(memeServiceDto.getMemberId());
		Meme meme = memeRepository.findById(memeServiceDto.getMemeId()).orElseThrow(NotFoundMemeException::new);

		if (meme.getMember() != member) {
			throw new AccessDeniedMemeException();
		}

		return MemeResponse.of(meme);
	}

	@Transactional
	public MemePageResponse findMemberMemePageResponse(MemeServiceDto memeServiceDto, Pageable pageable) {
		Member member = memberService.findMemberFromEmail(memeServiceDto.getEmail());

		Page<MemeResponse> memeList = memeRepository.findAllByMember(member, pageable);

		return MemePageResponse.builder()
			.totalPage(memeList.getTotalPages())
			.currentPage(pageable.getPageNumber())
			.memeList(memeList.getContent())
			.build();
	}

	@Transactional(readOnly = true)
	public Meme findMemeFromId(Long memeId) {
		return memeRepository.findById(memeId).orElseThrow(NotFoundMemeException::new);
	}
}
