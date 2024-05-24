package com.example.memetory.domain.like.service;

import static com.example.memetory.domain.like.LikeFixture.*;
import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.memetory.domain.like.dto.LikeServiceDto;
import com.example.memetory.domain.like.entity.Like;
import com.example.memetory.domain.like.exception.NotCreateLikeException;
import com.example.memetory.domain.like.repository.LikeRepository;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.service.MemesService;

@DisplayName("Like 서비스 테스트의 ")
@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {
	@Mock
	private MemberService memberService;
	@Mock
	private MemesService memesService;
	@Mock
	private LikeRepository likeRepository;
	@InjectMocks
	private LikeService likeService;

	private Member member;
	private Memes memes;
	private LikeServiceDto likeServiceDto;

	@BeforeEach
	void setUp() {
		member = MEMBER();
		memes = MEMES(member, MEME(member));
		likeServiceDto = LIKE_SERVICE_DTO();
	}

	@Test
	@DisplayName("likeServiceDto로 인한 Like 등록 성공")
	void Given_likeServiceDto_When_registerLike_Execute_likeRepository_save() {
		// given
		given(memberService.findMemberFromEmail(likeServiceDto.getEmail())).willReturn(member);
		given(memesService.findMemesFromMemesId(likeServiceDto.getMemesId())).willReturn(memes);

		// when
		likeService.registerLike(likeServiceDto);

		// then
		assertThat(memes.getLikeCount()).isEqualTo(2L);
		verify(likeRepository).save(any(Like.class));
	}

	@Test
	@DisplayName("Like 테이블의 중복된 Memes와 Member 칼럼으로 인한 NotCreateLikeException 반환")
	void Given_likeServiceDto_When_registerLike_Throw_NotCreateLikeException() {
		// given
		given(memberService.findMemberFromEmail(likeServiceDto.getEmail())).willReturn(member);
		given(memesService.findMemesFromMemesId(likeServiceDto.getMemesId())).willReturn(memes);
		given(likeRepository.save(any())).willThrow(new DataIntegrityViolationException(any()));

		// then
		assertThrows(NotCreateLikeException.class, () -> likeService.registerLike(likeServiceDto));
		verify(likeRepository).save(any(Like.class));
		assertThat(memes.getLikeCount()).isEqualTo(1L);
	}

	@Test
	@DisplayName("likeServiceDto로 인한 Like 삭제 성공")
	void Given_LikeServiceDto_when_cancelLike_Execute_likeRepository_delete() {
		// given
		Like like = LIKE(member, memes);
		given(memberService.findMemberFromEmail(likeServiceDto.getEmail())).willReturn(member);
		given(memesService.findMemesFromMemesId(likeServiceDto.getMemesId())).willReturn(memes);
		given(likeRepository.findLikeByMemberAndMemes(member, memes)).willReturn(Optional.ofNullable(like));

		// when
		likeService.cancelLike(likeServiceDto);

		// then
		assertThat(memes.getLikeCount()).isZero();
		verify(likeRepository).delete(like);
	}
}
