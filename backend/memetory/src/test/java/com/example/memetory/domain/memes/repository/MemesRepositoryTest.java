package com.example.memetory.domain.memes.repository;

import static com.example.memetory.domain.like.LikeFixture.*;
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
import com.example.memetory.domain.memes.dto.response.MemesInfoResponse;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.global.RepositoryTest;

@DisplayName("Memes 레포지토리 테스트의 ")
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
	@DisplayName("밈스 id를 통한 단일 밈스 조회 성공")
	void Given_Memes_When_findByMemesId_Then_Memes() {
		// given
		Memes expectedResult = memesRepository.save(MEMES(member, meme));

		// when 
		Memes result = memesRepository.findByMemesId(expectedResult.getId()).get();

		// then
		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	@DisplayName("Pageable을 통한 Slice<MemesInfo> 반환")
	void Given_Pageable_When_findMemesInfoSlice_Then_Slice_MemesInfo() {
		// given
		final int pageSize = 10;

		for (int i = 0; i < pageSize + 1; i++) {
			memesRepository.save(MEMES_SET_LIKE(member, meme, (long)i));
		}

		PageRequest page = PageRequest.of(0, pageSize);

		// when
		Slice<MemesInfoResponse> result = memesRepository.findMemesInfoSlice(page);

		// then
		assertThat(result.getContent()).hasSize(pageSize);
		assertTrue(result.hasNext());
	}

	@Test
	@DisplayName("전체 조회수 Top10 List<MemesInfo> 반환")
	void When_findTopMemesOrderByLikeCount_Then_List_MemesInfo() {
		// given
		for (int i = 0; i < 100; i++) {
			memesRepository.save(MEMES_SET_LIKE(memberRepository.save(MEMBER()), meme, (long)i));
		}

		// when
		List<MemesInfoResponse> result = memesRepository.findTopMemesOrderByLikeCount();

		// then
		assertThat(result).hasSize(10);
		assertThat(result.get(0).getLikeCount()).isEqualTo(99L);
		assertThat(result.get(1).getLikeCount()).isEqualTo(98L);
	}

	// TODO Like의 createdAt의 시간을 바꿔서 테스트를 진행해 봐야함.
	@Test
	@DisplayName("지난주를 통해 일주일동안 좋아요 Top 10 List<MemesInfo> 반한")
	void Given_lastWeek_When_findTopMemesOrderByLikeCountForPeriod_Then_List_MemesInfo() {
		// give
		saveLikesSetOtherTime();

		LocalDateTime lastWeek = LocalDateTime.now().minusWeeks(1);

		// when 실행
		List<MemesInfoResponse> result = memesRepository.findTopMemesOrderByLikeCountForPeriod(lastWeek);

		// then
		assertThat(result).hasSize(10);
		assertThat(result.get(0).getLikeCount()).isEqualTo(20L);
		assertThat(result.get(0).getMemberNickname()).isEqualTo(member.getNickname());
	}

	private void saveLikesSetOtherTime() {
		for (int i = 1; i <= 10; i++) {
			Memes savedMemes = memesRepository.save(MEMES(member, meme));

			for (int j = 0; j < i * 2; j++) {
				Member savedMember = memberRepository.save(MEMBER());
				Like saveLike = likeRepository.save(LIKE(savedMember, savedMemes));

				saveLike.getCreatedAt().minusDays(i);
			}
		}
	}

	@Test
	@DisplayName("다음주를 통해 좋아요 Top 10 빈 List<MemesInfo> 반환")
	void Given_nextWeek_When_findTopMemesOrderByLikeCountForPeriod_Then_List_MemesInfo() {
		// given
		Memes savedMemes = memesRepository.save(MEMES(member, meme));
		likeRepository.save(LIKE(member, savedMemes));

		LocalDateTime nextWeek = LocalDateTime.now().plusWeeks(1);

		// when
		List<MemesInfoResponse> result = memesRepository.findTopMemesOrderByLikeCountForPeriod(nextWeek);

		// then
		assertThat(result).isEmpty();
	}
}
