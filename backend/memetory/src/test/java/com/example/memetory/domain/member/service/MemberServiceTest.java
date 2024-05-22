package com.example.memetory.domain.member.service;

import static com.example.memetory.domain.member.MemberFixture.*;
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

import com.example.memetory.domain.member.dto.MemberServiceDto;
import com.example.memetory.domain.member.dto.response.MemberResponse;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.exception.DuplicatedMemberException;
import com.example.memetory.domain.member.exception.NotFoundMemberException;
import com.example.memetory.domain.member.repository.MemberRepository;

@DisplayName("memberService의 ")
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
	@InjectMocks
	private MemberService memberService;
	@Mock
	private MemberRepository memberRepository;

	private Member member;

	@BeforeEach
	void setUp() {
		member = MEMBER();
	}

	@Test
	@DisplayName("이메일을 통해서 멤버 가져오기 성공")
	void 이메일을_통해서_Member_불러오기() {
		// given 멤버 저장
		final String email = "junrain@ourservice.com";

		// when 실행
		when(memberRepository.findByEmail(email)).thenReturn(Optional.ofNullable(member));
		Member result = memberService.findMemberFromEmail(email);

		// then
		assertThat(result).isEqualTo(member);
	}

	@Test
	@DisplayName("이메일을 통해서 멤버 가져오기 실패")
	void 이메일을_통해서_Member_불러오기_실패() {
		// given
		final String email = "junrain@ourservice.com";
		given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

		// then
		assertThrows(NotFoundMemberException.class, () -> memberService.findMemberFromEmail(email));
	}

	@Test
	@DisplayName("ID를 통해서 멤버 가져오기")
	void ID를_통해서_Member_불러오기() {
		// given 멤버 저장
		final Long id = -1L;

		// when 실행
		when(memberRepository.findById(id)).thenReturn(Optional.ofNullable(member));
		Member result = memberService.findMemberFromId(id);

		// then
		assertThat(result).isEqualTo(member);
	}

	@Test
	@DisplayName("ID를 통해서 멤버 가져오기 실패")
	void ID를_통해서_Member_불러오기_실패() {
		// given 멤버 저장
		final Long id = -1L;
		given(memberRepository.findById(id)).willReturn(Optional.empty());

		// then
		assertThrows(NotFoundMemberException.class, () -> memberService.findMemberFromId(id));
	}

	@Test
	@DisplayName("Member의 업데이트 성공")
	void member_업데이트() {
		MemberServiceDto memberServiceDto = UPDATE_MEMBER_SERVICE_DTO();

		// when 멤버 업데이트
		when(memberRepository.existMemberByNickname(anyString())).thenReturn(false);
		when(memberRepository.findByEmail(memberServiceDto.getEmail())).thenReturn(Optional.ofNullable(member));
		memberService.updateMember(memberServiceDto);

		// then
		assertThat(member.getNickname()).isEqualTo(memberServiceDto.getNickname());
	}

	@Test
	@DisplayName("Member의 업데이트 실패")
	void member_업데이트_실패() {
		MemberServiceDto memberServiceDto = UPDATE_MEMBER_SERVICE_DTO();

		// when 멤버 업데이트
		when(memberRepository.existMemberByNickname(anyString())).thenReturn(true);

		// then
		assertThrows(DuplicatedMemberException.class, () -> memberService.updateMember(memberServiceDto));
	}

	@Test
	@DisplayName("단일 멤버 조회 성공")
	void member_조회_성공() {
		// given
		MemberServiceDto memberServiceDto = MEMBER_SERVICE_DTO();
		MemberResponse expectation = MemberResponse.of(member);

		// when
		when(memberRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(member));
		MemberResponse result = memberService.findMemberResponse(memberServiceDto);

		// then
		assertThat(expectation).usingRecursiveComparison().isEqualTo(result);
	}
}
