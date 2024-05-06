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
import com.example.memetory.domain.member.entity.Member;
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
		Member result = memberService.findByEmail(email);

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
		assertThrows(NotFoundMemberException.class, () -> memberService.findByEmail(email));
	}

	@Test
	@DisplayName("ID를 통해서 멤버 가져오기")
	void ID를_통해서_Member_불러오기() {
		// given 멤버 저장
		final Long id = -1L;

		// when 실행
		when(memberRepository.findById(id)).thenReturn(Optional.ofNullable(member));
		Member result = memberService.findById(id);

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
		assertThrows(NotFoundMemberException.class, () -> memberService.findById(id));
	}

	@Test
	@DisplayName("Member의 nickname 중복 여부 검사 true 반환")
	void nickname_중복검사_True() {
		// given
		final MemberServiceDto memberServiceDto = MEMBER_SERVICE_DTO();
		given(memberRepository.existsMemberByNickname(memberServiceDto.getNickname())).willReturn(true);

		// then 실행
		assertTrue(memberService.isDuplicatedNickname(MEMBER_SERVICE_DTO()));
	}

	@Test
	@DisplayName("Member의 업데이트가 잘 이루어지는가")
	void member_업데이트() {
		MemberServiceDto memberServiceDto = UPDATE_MEMBER_SERVICE_DTO();

		// when 멤버 업데이트
		when(memberRepository.findByEmail(memberServiceDto.getEmail())).thenReturn(Optional.ofNullable(member));
		memberService.update(memberServiceDto);

		// then
		assertThat(member.getNickname()).isEqualTo(memberServiceDto.getNickname());
	}
}
