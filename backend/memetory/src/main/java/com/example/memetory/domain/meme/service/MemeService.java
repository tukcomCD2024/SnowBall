package com.example.memetory.domain.meme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.repository.MemeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemeService {
	private final MemeRepository memeRepository;

	@Transactional
	public void save(Meme meme) {
		memeRepository.save(meme);
	}
}
