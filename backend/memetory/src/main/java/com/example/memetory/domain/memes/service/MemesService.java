package com.example.memetory.domain.memes.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.service.MemeService;
import com.example.memetory.domain.memes.dto.MemesServiceDto;
import com.example.memetory.domain.memes.entity.Memes;
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
		Member findMember = memberService.findByEmail(memesServiceDto.getEmail());
		Meme findMeme = memeService.getMemeBetweenService(memesServiceDto.getMemeId());

		Memes newMemes = memesServiceDto.toEntity(findMember, findMeme);
		memesRepository.save(newMemes);
	}

	@Transactional(readOnly = true)
	public Memes getMemesBetweenService(Long memesId) {
		return memesRepository.findById(memesId).orElseThrow(NotFoundMemesException::new);
	}
}
