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
import com.example.memetory.global.RepositoryTest;

import jakarta.transaction.Transactional;

@DisplayName("member 레포지토리 테스트의 ")
@RepositoryTest
public class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("이메일로 멤버 찾기")
	public void 이메일로_Member_찾기() {
		// given -> 멤버 저장하기
		Member savedMember = memberRepository.save(MEMBER());

		// when 멤버 찾기
		Optional<Member> findMember = memberRepository.findByEmail(savedMember.getEmail());

		// then 확인하기
		assertThat(savedMember).isEqualTo(findMember.get());
	}

	@Test
	@DisplayName("SocialType과 SocialId로 멤버 찾기")
	public void SocialType과_SocialId로_Member_찾기() {
		// given -> 멤버 저장하기
		Member savedMember = memberRepository.save(MEMBER());

		// when 멤버 찾기
		Optional<Member> findMember = memberRepository
			.findBySocialTypeAndSocialId(savedMember.getSocialType(), savedMember.getSocialId());

		// then 확인하기
		assertThat(savedMember).isEqualTo(findMember.get());
	}

	@Test
	@DisplayName("닉네임 존재하는지 확인하기")
	public void 닉네임_존재_여부() {
		// given -> 멤버 저장하기
		Member savedMember = memberRepository.save(MEMBER());

		// then 확인하기
		assertTrue(memberRepository.existsMemberByNickname(savedMember.getNickname()));
	}

	@Test
	@DisplayName("멤버 업데이트 확인 테스트")
	public void 멤버_업데이트() {
		// given
		Member savedMember = memberRepository.save(MEMBER());
		MemberServiceDto memberServiceDto = UPDATE_MEMBER_SERVICE_DTO();

		// when
		updatedMember(savedMember, memberServiceDto);

		// then
		assertThat(memberRepository.findById(savedMember.getId()).get().getNickname()).isEqualTo(
			memberServiceDto.getNickname());

	}

	@Transactional
	public void updatedMember(Member savedMember, MemberServiceDto memberServiceDto) {
		savedMember.update(memberServiceDto);
	}
}
