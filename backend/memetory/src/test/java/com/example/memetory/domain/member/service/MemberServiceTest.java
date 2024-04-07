package com.example.memetory.domain.member.service;

import static com.example.memetory.domain.member.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.entity.Role;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.jwt.service.JwtService;

@DisplayName("memberService의 ")
@Transactional
@SpringBootTest
public class MemberServiceTest {
	@Autowired
	private MemberService memberService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("회원가입이 잘 진행 되는가?")
	void 회원가입() {
		// given 멤버 저장
		Member savedMember = memberRepository.save(GUEST_MEMBER);

		// when 실행
		memberService.register(MEMBER_SERVICE_DTO);

		// then 확인 ROLE과 닉네임이 잘 변경 됐는지 확인
		Optional<Member> updatedMember = memberRepository.findByEmail(GUEST_MEMBER.getEmail());
		assertThat(MEMBER_SERVICE_DTO.getNickname()).isEqualTo(updatedMember.get().getNickname());
		assertThat(Role.USER).isEqualTo(updatedMember.get().getRole());
	}

	@Test
	@DisplayName("이메일을 통해서 멤버 가져오기")
	void 이메일을_통해서_Member_불러오기() {
		// given 멤버 저장
		Member savedMember = memberRepository.save(MEMBER);

		// when 실행
		Member findMember = memberService.findByEmail(savedMember.getEmail());

		// then
		assertThat(findMember.getId()).isEqualTo(savedMember.getId());

	}

	@Test
	@DisplayName("ID를 통해서 멤버 가져오기")
	void ID를_통해서_Member_불러오기() {
		// given 멤버 저장
		Member savedMember = memberRepository.save(MEMBER);

		// when 실행
		Member findMember = memberService.findById(savedMember.getId());

		// then
		assertThat(findMember.getId()).isEqualTo(savedMember.getId());
	}
}
