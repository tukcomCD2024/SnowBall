package com.example.memetory.domain.memes.repository;

import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.global.RepositoryTest;

@DisplayName("memes 레포지토리 테스트의 ")
@RepositoryTest
public class MemesRepositoryTest {
	@Autowired
	private MemesRepository memesRepository;

	private Member member;
	private Meme meme;

	@BeforeEach
	void setUp() {
		member = MEMBER();
		meme = MEME(member);
	}

	@Test
	@DisplayName("Memes의 저장 및 단일 조회 테스트")
	void MEMES_단일조회() {
		// given Memes 세팅
		Memes savedMemes = memesRepository.save(MEMES(member, meme));

		// then
		assertThat(memesRepository.findById(savedMemes.getId()).get()).isEqualTo(savedMemes);
	}
}
