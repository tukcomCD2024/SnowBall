package com.example.memetory.domain.like.service;

import static com.example.memetory.domain.like.LikeFixture.*;
import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.memetory.domain.like.dto.LikeServiceDto;
import com.example.memetory.domain.like.entity.Like;
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
	@DisplayName("register 성공 테스트")
	void 좋아요_등록() {
		// given
		given(memberService.findByEmail(likeServiceDto.getEmail())).willReturn(member);
		given(memesService.getMemesBetweenService(likeServiceDto.getMemesId())).willReturn(memes);

		// when
		likeService.register(likeServiceDto);

		// then
		assertThat(memes.getLikeCount()).isEqualTo(2L);
		verify(likeRepository).save(any(Like.class));
	}

	@Test
	@DisplayName("cancel 성공 테스트")
	void 좋아요_취소() {
		// given
		Like like = LIKE(member, memes);
		given(memberService.findByEmail(likeServiceDto.getEmail())).willReturn(member);
		given(memesService.getMemesBetweenService(likeServiceDto.getMemesId())).willReturn(memes);
		given(likeRepository.findLikeByMemberAndMemes(member, memes)).willReturn(Optional.ofNullable(like));

		// when
		likeService.cancel(likeServiceDto);

		// then
		assertThat(memes.getLikeCount()).isEqualTo(0L);
		verify(likeRepository).delete(like);
	}
}
