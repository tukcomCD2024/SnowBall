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

import com.example.memetory.domain.like.entity.Like;
import com.example.memetory.domain.like.repository.LikeRepository;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.repository.MemeRepository;
import com.example.memetory.domain.memes.dto.response.MemesInfo;
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
	@Autowired
	private LikeRepository likeRepository;

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

		// when 
		Memes expect = memesRepository.findByMemesId(savedMemes.getId()).get();

		// then
		assertThat(expect).isEqualTo(savedMemes);
		assertThat(expect.getMember()).isEqualTo(member);
		assertThat(expect.getMeme()).isEqualTo(meme);
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
		Slice<MemesInfo> result1 = memesRepository.findAllMemesSlice(page1);
		Slice<MemesInfo> result2 = memesRepository.findAllMemesSlice(page2);

		// then
		assertThat(result1.getContent()).hasSize(10);
		assertFalse(result2.hasNext());
	}

	@Test
	@DisplayName("Memes의 좋아요 Top 10 테스트")
	void MEMES_TOP_10() {
		// given Memes 100 세팅
		for (int i = 0; i < 100; i++) {
			memesRepository.save(MEMES_SET_LIKE(memberRepository.save(MEMBER()), meme, (long)i));
		}
		// when 실행
		List<MemesInfo> result = memesRepository.findTopMemesOrderByLikeCount();

		// then
		assertThat(result).hasSize(10);
		assertThat(result.get(0).getLikeCount()).isEqualTo(99L);
		assertThat(result.get(1).getLikeCount()).isEqualTo(98L);
	}

	@Test
	@DisplayName("Memes의 주별 좋아요 Top 10 테스트")
	void MEMES_TOP_10_WEEK() {
		// given Memes 100 세팅
		for (int i = 1; i <= 10; i++) {
			Memes savedMemes = memesRepository.save(MEMES(member, meme));
			for (int j = 0; j < i * 2; j++) {
				likeRepository.save(Like.builder()
					.memes(savedMemes)
					.member(memberRepository.save(MEMBER()))
					.build());
			}
		}
		LocalDateTime time = LocalDateTime.now().minusWeeks(1);

		// when 실행
		List<MemesInfo> result = memesRepository.findTopMemesByLikeCountForPeriod(time);

		// then
		assertThat(result).hasSize(10);
		assertThat(result.get(0).getLikeCount()).isEqualTo(20L);
		assertThat(result.get(0).getMemberNickname()).isEqualTo(member.getNickname());
	}

	@Test
	@DisplayName("Memes의 주별 좋아요 Top 10 해당하는 시간 없음")
	void MEMES_TOP_10_AFTER_WEEK() {
		// given Memes 100 세팅
		Memes savedMemes = memesRepository.save(MEMES(member, meme));
		likeRepository.save(Like.builder()
			.memes(savedMemes)
			.member(member)
			.build());

		LocalDateTime time = LocalDateTime.now().plusWeeks(1);

		// when 실행
		List<MemesInfo> result = memesRepository.findTopMemesByLikeCountForPeriod(time);

		// then
		assertThat(result).hasSize(0);
	}
}
