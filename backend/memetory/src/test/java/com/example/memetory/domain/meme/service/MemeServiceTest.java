package com.example.memetory.domain.meme.service;

import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.dto.MemePageResponse;
import com.example.memetory.domain.meme.dto.MemeResponse;
import com.example.memetory.domain.meme.dto.MemeServiceDto;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.exception.NotFoundMemeException;
import com.example.memetory.domain.meme.repository.MemeRepository;

@DisplayName("memeService 테스트의 ")
@ExtendWith(MockitoExtension.class)
public class MemeServiceTest {
	@InjectMocks
	private MemeService memeService;

	@Mock
	private MemberService memberService;
	@Mock
	private MemeRepository memeRepository;

	private Member member;
	private Meme meme;

	@BeforeEach
	void setUp() {
		member = MEMBER();
		meme = MEME(member);
	}

	@Test
	@DisplayName("밈 저장 성공")
	void 밈_저장() {
		// given Meme을 세팅
		MemeServiceDto memeServiceDto = MEME_SERVICE_DTO();

		// when
		when(memberService.findById(memeServiceDto.getMemberId())).thenReturn(member);
		when(memeRepository.save(any())).thenReturn(meme);

		MemeResponse result = memeService.register(memeServiceDto);

		// then
		assertThat(result).isInstanceOf(MemeResponse.class);
	}

	@Test
	@DisplayName("Meme ID를 통해 MemeResponse를 반환")
	void 단일_밈_조회() {
		// given 밈 저장, memServiceDto, memeReponse 생성
		MemeResponse expected = MemeResponse.of(meme);
		MemeServiceDto memeServiceDto = MEME_SERVICE_DTO();

		// when
		when(memeRepository.findById(memeServiceDto.getMemeId())).thenReturn(Optional.ofNullable(meme));
		MemeResponse result = memeService.getMeme(memeServiceDto);

		// given
		assertThat(result).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	@DisplayName("Meme ID를 통해 MemeResponse를 반환 실패")
	void 단일_밈_조회_실패() {
		// given 밈 저장, memServiceDto, memeReponse 생성
		MemeServiceDto memeServiceDto = MEME_SERVICE_DTO();

		// when
		when(memeRepository.findById(memeServiceDto.getMemeId())).thenReturn(Optional.empty());

		// given
		assertThrows(NotFoundMemeException.class, () -> memeService.getMeme(memeServiceDto));
	}

	@Test
	@DisplayName("Member를 통한 해당 멤버의 전체 밈 조회")
	void 전체_밈_조회() {
		// given 밈들 저장, MemeListResponse 세팅
		Pageable page = PageRequest.of(0, 10);
		MemeServiceDto memeServiceDto = MEME_SERVICE_DTO();

		Member otherMember = SECOND_MEMBER();
		Meme secondtMeme = SECOND_MEME(member);
		Meme anotherMemberMeme = MEME(otherMember);


		List<MemeResponse> memeList = List.of(meme, secondtMeme, anotherMemberMeme)
			.stream()
			.map(MemeResponse::of)
			.toList();

		Page<MemeResponse> returned = new PageImpl<>(memeList);

		MemePageResponse expected = MemePageResponse.builder()
			.totalPage(1)
			.currentPage(0)
			.memeList(memeList)
			.build();
		
		// when
		when(memeRepository.findAllByMember(any(), any())).thenReturn(returned);
		MemePageResponse result = memeService.getAllMeme(memeServiceDto, page);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(expected);
	}
}
