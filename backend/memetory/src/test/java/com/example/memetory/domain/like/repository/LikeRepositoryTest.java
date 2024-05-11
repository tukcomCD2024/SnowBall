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
	@DisplayName("Member와 Memes로 Like 찾기")
	void Like_찾기() {
		// given
		Member member = MEMBER();
		memberRepository.save(member);
		Meme meme = MEME(member);
		memeRepository.save(meme);
		Memes memes = MEMES(member, meme);
		memesRepository.save(memes);
		Like like = LIKE(member, memes);
		Like expect = likeRepository.save(like);

		// when
		Optional<Like> result = likeRepository.findLikeByMemberAndMemes(member, memes);

		// then
		assertThat(expect).isEqualTo(result.get());
	}

	@Test
	@DisplayName("밈스와 멤버 조합 중복 감지")
	void 중복감지() {
		// given
		Member member = memberRepository.save(MEMBER());
		Meme meme = memeRepository.save(MEME(member));
		Memes memes = memesRepository.save(MEMES(member, meme));

		// when
		likeRepository.save(LIKE(member, memes));

		// then
		assertThrows(RuntimeException.class, () -> likeRepository.save(LIKE(member, memes)));
	}
}
