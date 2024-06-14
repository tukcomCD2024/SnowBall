package com.example.memetory.domain.meme.service;

import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.exception.AccessDeniedException;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.dto.MemePageResponse;
import com.example.memetory.domain.meme.dto.MemeResponse;
import com.example.memetory.domain.meme.dto.MemeServiceDto;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.exception.NotFoundMemeException;
import com.example.memetory.domain.meme.repository.MemeRepository;
import com.example.memetory.global.firebase.service.FirebaseService;

@DisplayName("memeService 테스트의 ")
@ExtendWith(MockitoExtension.class)
public class MemeServiceTest {
	@InjectMocks
	private MemeService memeService;

	@Mock
	private MemberService memberService;
	@Mock
	private MemeRepository memeRepository;
	@Mock
	private FirebaseService firebaseService;

	private Member member;
	private Meme meme;

	@BeforeEach
	void setUp() {
		member = MEMBER();
		meme = MEME(member);
	}

	@Test
	@DisplayName("MemesServiceDto를 통한 밈 저장 성공")
	void Given_MemesServiceDto_When_registerMeme_Then_Meme() {
		// given Meme을 세팅
		MemeServiceDto memeServiceDto = MEME_SERVICE_DTO();
		Long memberId = memeServiceDto.getMemberId();
		MemeResponse expectedResult = MemeResponse.of(meme);

		given(memberService.findMemberFromId(memberId)).willReturn(member);
		given(memeRepository.save(any(Meme.class))).willReturn(meme);
		doNothing().when(firebaseService).sendMessage(any());

		// when
		MemeResponse result = memeService.registerMeme(memeServiceDto);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
	}

	@Test
	@DisplayName("id를 통한 멤버의 MemeResponse를 반환 성공")
	void Given_id_When_findMemberMemeResponse_Then_Member_MemeResponse() {
		// given 밈 저장, memServiceDto, memeReponse 생성
		MemeResponse expectedResult = MemeResponse.of(meme);
		MemeServiceDto memeServiceDto = MEME_SERVICE_DTO();

		given(memberService.findMemberFromEmail(memeServiceDto.getEmail())).willReturn(member);
		given(memeRepository.findById(memeServiceDto.getMemeId())).willReturn(Optional.ofNullable(meme));

		// when
		MemeResponse result = memeService.findMemberMemeResponse(memeServiceDto);

		// given
		assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
	}

	@Test
	@DisplayName("존재하지 않는 id로 인한 NotFoundMemeException 반환")
	void Given_notExistId_When_findMemberMemeResponse_Throw_NotFoundMemberMemeException() {
		// given
		MemeServiceDto memeServiceDto = MEME_SERVICE_DTO();
		Long notExistId = memeServiceDto.getMemeId();

		given(memeRepository.findById(notExistId)).willReturn(Optional.empty());

		// given
		assertThrows(NotFoundMemeException.class, () -> memeService.findMemberMemeResponse(memeServiceDto));
	}

	@Test
	@DisplayName("Member를 통한 멤버의 Page<MemeResponse> 조회 성공")
	void Given_Member_When_findMemberMemePageResponse_Then_Page_Member_MemeResponse() {
		// given
		final int pageSize = 10;
		Pageable page = PageRequest.of(0, pageSize);
		MemeServiceDto memeServiceDto = MEME_SERVICE_DTO();

		List<MemeResponse> memeList = createMemeResponseList(pageSize);

		Page<MemeResponse> returnedMemeResponse = new PageImpl<>(memeList);

		MemePageResponse expectedResult = MemePageResponse.builder()
			.totalPage(1)
			.currentPage(0)
			.memeList(memeList)
			.build();

		given(memeRepository.findAllByMember(any(), any())).willReturn(returnedMemeResponse);

		// when
		MemePageResponse result = memeService.findMemberMemePageResponse(memeServiceDto, page);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
	}

	private List<MemeResponse> createMemeResponseList(int pageSize) {
		List<MemeResponse> memeResponseList = new ArrayList<>();

		for (int i = 0; i < pageSize; i++) {
			memeResponseList.add(MemeResponse.of(MEME(member)));
		}

		return memeResponseList;
	}

	@Test
	@DisplayName("밈 멤버와 다른 멤버로 인한 AccessDeniedMemeException 반환")
	void Given_differentMember_When_certifyMemeMember_Throw_AccessDeniedMemeException() {
		// given
		Member differentMember = OTHER_MEMBER();

		// then
		assertThrows(AccessDeniedException.class, () -> memberService.certifyMember(member, differentMember));
	}

}
