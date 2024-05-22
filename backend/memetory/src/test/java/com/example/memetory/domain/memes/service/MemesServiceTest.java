package com.example.memetory.domain.memes.service;

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

import com.example.memetory.domain.member.dto.response.MemberResponse;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.service.MemeService;
import com.example.memetory.domain.memes.dto.MemesServiceDto;
import com.example.memetory.domain.memes.dto.response.MemesResponse;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.exception.NotDeleteMemesException;
import com.example.memetory.domain.memes.exception.NotFoundMemesException;
import com.example.memetory.domain.memes.repository.MemesRepository;

@DisplayName("memesService 테스트의 ")
@ExtendWith(MockitoExtension.class)
public class MemesServiceTest {
	protected MemesServiceDto memesServiceDto;
	@InjectMocks
	private MemesService memesService;
	@Mock
	private MemberService memberService;
	@Mock
	private MemesRepository memesRepository;
	@Mock
	private MemeService memeService;
	private Member member;
	private Meme meme;
	private Memes memes;

	@BeforeEach
	void setUp() {
		member = MEMBER();
		meme = MEME(member);
		memes = MEMES(member, meme);
		memesServiceDto = MEMES_SERVICE_DTO();
	}

	@Test
	@DisplayName("밈스 저장 성공")
	void 밈스_저장() {
		// when
		when(memberService.findByEmail(memesServiceDto.getEmail())).thenReturn(member);
		when(memeService.getMemeBetweenService(memesServiceDto.getMemeId())).thenReturn(meme);

		memesService.register(memesServiceDto);

		// then
		verify(memesRepository).save(any());
	}

	@Test
	@DisplayName("밈스 삭제 성공")
	void 밈스_삭제() {
		// given
		given(memesRepository.findByMemesId(any())).willReturn(Optional.ofNullable(memes));
		given(memberService.findByEmail(any())).willReturn(member);

		// when
		memesService.delete(memesServiceDto);

		// then
		verify(memesRepository).delete(any());
	}

	@Test
	@DisplayName("밈스 삭제 실패")
	void 밈스_삭제_실패() {
		// given
		given(memesRepository.findByMemesId(any())).willReturn(Optional.ofNullable(memes));
		given(memberService.findByEmail(any())).willReturn(any());

		// when
		assertThrows(NotDeleteMemesException.class, () -> memesService.delete(memesServiceDto));
	}

	@Test
	@DisplayName("밈스 단일조회 성공")
	void 밈스_단일_조회() {
		// given
		given(memesRepository.findByMemesId(any())).willReturn(Optional.ofNullable(memes));
		MemesResponse result = memesService.getMemesResponse(memesServiceDto);

		// when
		assertThat(result.getMember().getMemberId()).isEqualTo(MemberResponse.of(member).getMemberId());
	}

	@Test
	@DisplayName("밈스 단일조회 실패")
	void 밈스_단일_조회_실패() {
		// given
		given(memesRepository.findByMemesId(any())).willReturn(Optional.ofNullable(any()));

		// then
		assertThrows(NotFoundMemesException.class, () -> memesService.getMemesResponse(memesServiceDto));
	}
}
