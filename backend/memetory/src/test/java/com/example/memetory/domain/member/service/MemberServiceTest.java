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
import com.example.memetory.domain.member.exception.DeniedAccessException;
import com.example.memetory.domain.member.exception.DuplicatedMemberException;
import com.example.memetory.domain.member.exception.NotFoundMemberException;
import com.example.memetory.domain.member.repository.MemberRepository;

@DisplayName("Member 서비스 테스트의 ")
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
	@DisplayName("이메일을 통한 멤버 반환 성공")
	void Given_email_When_findMember_Then_Member() {
		// given 멤버 저장
		final String email = member.getEmail();
		given(memberRepository.findByEmail(email)).willReturn(Optional.ofNullable(member));

		// when 실행
		Member result = memberService.findMemberFromEmail(email);

		// then
		assertThat(result).isEqualTo(member);
	}

	@Test
	@DisplayName("존재하지 않는 이메일로 인한 NotFoundMemberException 반환")
	void Given_NotExistEmail_When_findMember_Throw_NotFoundMemberException() {
		// given
		final String email = "NotExistEmail";

		given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

		// then
		assertThrows(NotFoundMemberException.class, () -> memberService.findMemberFromEmail(email));
	}

	@Test
	@DisplayName("id를 통한 멤버 반환 성공")
	void Given_Id_When_findById_Then_Member() {
		// given
		final Long id = -1L;

		given(memberRepository.findById(id)).willReturn(Optional.ofNullable(member));

		// when
		Member result = memberService.findMemberFromId(id);

		// then
		assertThat(result).isEqualTo(member);
	}

	@Test
	@DisplayName("존재하지 않는 id로 인한 NotFoundMemberException 반환")
	void Given_NotExistId_When_findById_Throw_NotFoundMemberException() {
		// given
		final Long id = -1L;
		given(memberRepository.findById(id)).willReturn(Optional.empty());

		// then
		assertThrows(NotFoundMemberException.class, () -> memberService.findMemberFromId(id));
	}

	@Test
	@DisplayName("MemberServiceDto로 인한 Member 업데이트 성공")
	void Given_MemberServiceDto_When_updateMember_Then_Member() {
		MemberServiceDto memberServiceDto = UPDATED_MEMBER_SERVICE_DTO();
		String expectedNickname = memberServiceDto.getNickname();

		given(memberRepository.existMemberByNickname(anyString())).willReturn(false);
		given(memberRepository.findByEmail(memberServiceDto.getEmail())).willReturn(Optional.ofNullable(member));

		// when 멤버 업데이트
		memberService.updateMember(memberServiceDto);
		String resultNickname = member.getNickname();

		// then
		assertThat(resultNickname).isEqualTo(expectedNickname);
	}

	@Test
	@DisplayName("중복된 닉네임으로 인한 DuplicatedMemberException 반환")
	void Given_MemberServiceDto_When_updateMember_Throw_DuplicatedMemberException() {
		// given
		MemberServiceDto memberServiceDto = UPDATED_MEMBER_SERVICE_DTO();
		String duplicatedNickname = memberServiceDto.getNickname();

		given(memberRepository.existMemberByNickname(duplicatedNickname)).willReturn(true);

		// then
		assertThrows(DuplicatedMemberException.class, () -> memberService.updateMember(memberServiceDto));
	}

	@Test
	@DisplayName("MemberServiceDto를 통한 MemberResponse 반환 성공")
	void Given_MemberServiceDto_When_findMemberResponse_Then_MemberResponse() {
		// given
		MemberServiceDto memberServiceDto = MEMBER_SERVICE_DTO();
		MemberResponse expectedResult = MemberResponse.of(member);

		given(memberRepository.findByEmail(memberServiceDto.getEmail())).willReturn(Optional.ofNullable(member));

		// when
		MemberResponse result = memberService.findMemberResponse(memberServiceDto);

		// then
		assertThat(expectedResult).usingRecursiveComparison().isEqualTo(result);
	}

	@Test
	@DisplayName("다른 두 멤버로 인한 AccessDeniedException 반환")
	void Given_differentMember_When_certifyMember_Throw_AccessDeniedException() {
		// given
		Member m1 = MEMBER();
		Member m2 = OTHER_MEMBER();

		// then
		assertThrows(DeniedAccessException.class, () -> memberService.certifyMember(m1, m2));
	}
}
