package com.example.memetory.domain.memes.repository;

import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.repository.MemeRepository;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.global.RepositoryTest;

@DisplayName("memes 레포지토리 테스트의 ")
@RepositoryTest
public class MemesRepositoryTest {
	@Autowired
	private MemesRepository memesRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemeRepository memeRepository;

	private Member member;
	private Meme meme;

	@BeforeEach
	void setUp() {
		member = memberRepository.save(MEMBER());
		meme = memeRepository.save(MEME(member));
	}

	@Test
	@DisplayName("Memes의 저장 및 단일 조회 테스트")
	void MEMES_단일조회() {
		// given Memes 세팅
		Memes savedMemes = memesRepository.save(MEMES(member, meme));

		// then
		assertThat(memesRepository.findByMemesId(savedMemes.getId())).contains(savedMemes);
	}

	@Test
	@DisplayName("Memes의 Slice 테스트")
	void MEMES_슬라이스_조회() {
		// given Memes 100 세팅
		for (int i = 0; i < 100; i++) {
			memesRepository.save(MEMES_SET_LIKE(member, meme, (long)i));
		}
		PageRequest page1 = PageRequest.of(0, 10);
		PageRequest page2 = PageRequest.of(9, 10);
		// when 실행
		Slice<Memes> result1 = memesRepository.findMemesBy(page1);
		Slice<Memes> result2 = memesRepository.findMemesBy(page2);

		// then
		assertThat(result1.getContent()).hasSize(10);
		assertFalse(result2.hasNext());
	}

	@Test
	@DisplayName("Memes의 좋아요 Top 10 테스트")
	void MEMES_TOP_10() {
		// given Memes 100 세팅
		for (int i = 0; i < 100; i++) {
			memesRepository.save(MEMES_SET_LIKE(member, meme, (long)i));
		}
		PageRequest page = PageRequest.of(0, 10);
		// when 실행
		List<Memes> result = memesRepository.findTopMemesByLikeCount(page);

		// then
		assertThat(result).hasSize(10);
		assertThat(result.get(0).getLikeCount()).isEqualTo(99L);
	}

	@Test
	@DisplayName("Memes의 주별 좋아요 Top 10 테스트")
	void MEMES_TOP_10_WEEK() {
		// given Memes 100 세팅
		for (int i = 0; i < 100; i++) {
			memesRepository.save(MEMES_SET_LIKE(member, meme, (long)i));
		}
		LocalDateTime time = LocalDateTime.now().minusWeeks(1);
		PageRequest page = PageRequest.of(0, 10);
		// when 실행
		List<Memes> result = memesRepository.findTopMemesByLikeCountForPeriod(page, time);

		// then
		assertThat(result).hasSize(10);
	}
}
