package com.example.memetory.domain.like.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.like.dto.LikeServiceDto;
import com.example.memetory.domain.like.entity.Like;
import com.example.memetory.domain.like.exception.NotCreateLikeException;
import com.example.memetory.domain.like.exception.NotFoundLikeException;
import com.example.memetory.domain.like.repository.LikeRepository;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.service.MemesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
	private final MemberService memberService;
	private final MemesService memesService;
	private final LikeRepository likeRepository;

	@Transactional
	public void registerLike(LikeServiceDto likeServiceDto) {
		Member member = memberService.findMemberFromEmail(likeServiceDto.getEmail());
		Memes memes = memesService.findMemesFromMemesId(likeServiceDto.getMemesId());

		Like newLike = likeServiceDto.toEntityFromMemberAndMemes(member, memes);
		saveLike(newLike);
		memes.addLikeCount();
	}

	private void saveLike(Like like) {
		try {
			likeRepository.save(like);
		} catch (DataIntegrityViolationException e) {
			throw new NotCreateLikeException();
		}
	}

	@Transactional
	public void cancelLike(LikeServiceDto likeServiceDto) {
		Member member = memberService.findMemberFromEmail(likeServiceDto.getEmail());
		Memes memes = memesService.findMemesFromMemesId(likeServiceDto.getMemesId());
		Like like = likeRepository.findLikeByMemberAndMemes(member, memes).orElseThrow(NotFoundLikeException::new);

		likeRepository.delete(like);
		memes.cancelLikeCount();
	}
}
