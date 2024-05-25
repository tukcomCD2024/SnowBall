package com.example.memetory.domain.member.repository;

import static com.example.memetory.domain.member.MemberFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.memetory.domain.member.dto.MemberServiceDto;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.entity.SocialType;
import com.example.memetory.global.RepositoryTest;

import jakarta.transaction.Transactional;

@DisplayName("Member 레포지토리 테스트의 ")
@RepositoryTest
public class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("이메일을 통한 멤버 반환 성공")
	public void Given_email_When_findByEmail_Then_Member() {
		// given
		Member savedMember = memberRepository.save(MEMBER());
		String email = savedMember.getEmail();

		// when 멤버 찾기
		Optional<Member> findMember = memberRepository.findByEmail(email);

		// then 확인하기
		assertThat(savedMember).isEqualTo(findMember.get());
	}

	@Test
	@DisplayName("SocialType과 SocialId을 통한 멤버 반환 성공")
	public void Given_SocialTypeAndSocialId_Then_findBySocialTypeAndSocialId_Then_Member() {
		// given -> 멤버 저장하기
		Member savedMember = memberRepository.save(MEMBER());
		SocialType socialType = savedMember.getSocialType();
		String socialId = savedMember.getSocialId();

		// when
		Optional<Member> findMember = memberRepository.findBySocialTypeAndSocialId(socialType, socialId);

		// then
		assertThat(savedMember).isEqualTo(findMember.get());
	}

	@Test
	@DisplayName("닉네임이 존재해서 true 반환 성공")
	public void Given_nickName_When_findByNickName_Then_True() {
		// given
		Member savedMember = memberRepository.save(MEMBER());
		String nickName = savedMember.getNickname();

		// when
		boolean result = memberRepository.existMemberByNickname(nickName);

		// then 확인하기
		assertTrue(result);
	}

	@Test
	@DisplayName("MemberServiceDto를 통한 멤버 업데이트 성공")
	public void Given_MemberServiceDto_When_update() {
		// given
		Member savedMember = memberRepository.save(MEMBER());
		MemberServiceDto memberServiceDto = UPDATED_MEMBER_SERVICE_DTO();

		// when
		Member expectedMember = updatedMember(savedMember, memberServiceDto);
		Member result = memberRepository.findById(savedMember.getId()).get();

		// then
		assertThat(result).isEqualTo(expectedMember);
	}

	@Transactional
	public Member updatedMember(final Member member,final MemberServiceDto memberServiceDto) {
		member.update(memberServiceDto);
		return member;
	}
}
