package com.example.memetory.domain.meme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.dto.MemeServiceDto;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.repository.MemeRepository;

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
}
