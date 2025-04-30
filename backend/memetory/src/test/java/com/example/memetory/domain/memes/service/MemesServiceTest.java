package com.example.memetory.domain.memes.service;

import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.exception.DeniedAccessException;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.service.MemeService;
import com.example.memetory.domain.memes.dto.MemesRankDto;
import com.example.memetory.domain.memes.dto.MemesServiceDto;
import com.example.memetory.domain.memes.dto.response.MemesInfoResponse;
import com.example.memetory.domain.memes.dto.response.MemesResponse;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.exception.NotFoundMemesException;
import com.example.memetory.domain.memes.repository.MemesRepository;

@DisplayName("Memes 서비스 테스트의 ")
@ExtendWith(MockitoExtension.class)
public class MemesServiceTest {
	@InjectMocks
	private MemesService memesService;
	@Mock
	private MemberService memberService;
	@Mock
	private MemesRepository memesRepository;
	@Mock
	private MemeService memeService;
	@Mock
	private RankingService rankingService;

	private MemesServiceDto memesServiceDto;
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
	@DisplayName("MemesServiceDto를 통한 Memes 저장 성공")
	void Given_MemesServiceDto_When_registerMemes_Then_MemesResponse() {
		// given
		MemesResponse expectedResult = MemesResponse.of(memes);

		given(memberService.findMemberFromEmail(memesServiceDto.getEmail())).willReturn(member);
		given(memeService.findMemeFromId(memesServiceDto.getMemeId())).willReturn(meme);
		given(memesRepository.save(any(Memes.class))).willReturn(memes);

		// when
		MemesResponse result = memesService.registerMemes(memesServiceDto);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
	}

	@Test
	@DisplayName("권한 없는 멤버로 인한 NotAccessMemeException 반환")
	void Given_unAuthorizedMember_When_registerMemes_Throw_NotAccessMemeException() {
		// given
		Member unAuthorizedMember = OTHER_MEMBER();

		given(memeService.findMemeFromId(memesServiceDto.getMemeId())).willReturn(meme);
		given(memberService.findMemberFromEmail(any(String.class))).willReturn(unAuthorizedMember);

		doThrow(new DeniedAccessException()).when(memberService)
			.certifyMember(any(Member.class), any(Member.class));

		// then
		assertThrows(DeniedAccessException.class, () -> memesService.registerMemes(memesServiceDto));
	}

	@Test
	@DisplayName("MemesServiceDto를 통한 밈스 제거 성공")
	void Given_MemesServiceDto_When_deleteMemes_Execute_MemesRepository_delete() {
		// given
		given(memesRepository.findByMemesId(any())).willReturn(Optional.ofNullable(memes));
		given(memberService.findMemberFromEmail(any())).willReturn(member);

		// when
		memesService.deleteMemes(memesServiceDto);

		// then
		verify(memesRepository).delete(any());
	}

	@Test
	@DisplayName("권한 없는 멤버로 인한 DeniedAccessException 반환")
	void Given_unAuthorizedMember_When_deleteMemes_Throw_NotAccessMemesException() {
		// given
		Member unAuthorizedMember = OTHER_MEMBER();

		given(memesRepository.findByMemesId(any())).willReturn(Optional.ofNullable(memes));
		given(memberService.findMemberFromEmail(any())).willReturn(unAuthorizedMember);
		doThrow(new DeniedAccessException()).when(memberService).certifyMember(any(), any());

		// then
		assertThrows(DeniedAccessException.class, () -> memesService.deleteMemes(memesServiceDto));
	}

	@Test
	@DisplayName("MemesServiceDto를 통한 단일 MemesResponse 반환 성공")
	void Given_MemesServiceDto_When_findMemesResponse_Then_MemesResponse() {
		// given
		MemesResponse expectedResult = MemesResponse.of(memes);

		given(memesRepository.findByMemesId(any())).willReturn(Optional.ofNullable(memes));
		MemesResponse result = memesService.findMemesResponse(memesServiceDto);

		// when
		assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
	}

	@Test
	@DisplayName("존재하지 않는 MemesId로 인한 NotFoundMemesException 반환")
	void Given_notExistMemesId_When_findMemesResponse_Throw_MemesResponse() {
		// given
		Long notExistMemesId = -1L;

		given(memesRepository.findByMemesId(notExistMemesId)).willReturn(Optional.empty());

		// then
		assertThrows(NotFoundMemesException.class, () -> memesService.findMemesResponse(memesServiceDto));
	}

	@Test
	@DisplayName("주간 탑 10 조회로 인한 List<MemesInfoResponse> 반환")
	void When_findTopMemesByLikeForWeek_Then_List_MemesInfoResponse() {
		// given
		List<MemesRankDto> memesRankDtoList = generateMemesRankDtoList();
		given(rankingService.findDailyRanking()).willReturn(memesRankDtoList);
		given(memesRepository.findByMemesId(any())).willReturn(Optional.ofNullable(MEMES(member, meme)));

		// when
		List<MemesInfoResponse> result = memesService.findTopMemesByLikeForWeek();

		// then
		assertThat(result).hasSize(10);
		assertThat(result.get(0)).isInstanceOf(MemesInfoResponse.class);
	}

	private List<MemesRankDto> generateMemesRankDtoList() {
		List<MemesRankDto> expectedResult = new ArrayList<>();

		for (long i = 1; i <= 10; i++) {
			expectedResult.add(new MemesRankDto(i, i));
		}
		return expectedResult;
	}
}
