package com.example.memetory.domain.like.repository;

import static com.example.memetory.domain.like.LikeFixture.*;
import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.memetory.domain.like.entity.Like;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.repository.MemeRepository;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.repository.MemesRepository;
import com.example.memetory.global.RepositoryTest;

@DisplayName("like 레포지토리 테스트의 ")
@RepositoryTest
public class LikeRepositoryTest {
	@Autowired
	private LikeRepository likeRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemesRepository memesRepository;
	@Autowired
	private MemeRepository memeRepository;

	@Test
	@DisplayName("Member와 Memes를 통한 단일 Like 조회 성공")
	void Given_MemberAndMemes_When_findLikeByMemberAndMemes_Then_Like() {
		// given
		Member member = memberRepository.save(MEMBER());
		Meme meme = memeRepository.save(MEME(member));
		Memes memes = memesRepository.save(MEMES(member, meme));

		Like expectedResult = likeRepository.save(LIKE(member, memes));

		// when
		Optional<Like> result = likeRepository.findLikeByMemberAndMemes(member, memes);

		// then
		assertThat(expectedResult).isEqualTo(result.get());
	}

	@Test
	@DisplayName("중복된 밈스와 멤버 테이블로 인한 DataIntegrityViolationException 반환")
	void Given_Like_When_save_Throw_RuntimeException() {
		// given
		Member member = memberRepository.save(MEMBER());
		Meme meme = memeRepository.save(MEME(member));
		Memes memes = memesRepository.save(MEMES(member, meme));

		// when
		likeRepository.save(LIKE(member, memes));

		// then
		assertThrows(DataIntegrityViolationException.class, () -> likeRepository.save(LIKE(member, memes)));
	}
}
