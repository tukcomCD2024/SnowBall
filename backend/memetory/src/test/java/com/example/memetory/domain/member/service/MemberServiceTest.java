package com.example.memetory.domain.member.service;

import static com.example.memetory.domain.member.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.member.dto.MemberServiceDto;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;

@DisplayName("memberService의 ")
@Transactional
@SpringBootTest
public class MemberServiceTest {
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("이메일을 통해서 멤버 가져오기")
	void 이메일을_통해서_Member_불러오기() {
		// given 멤버 저장
		Member savedMember = memberRepository.save(MEMBER());

		// when 실행
		Member findMember = memberService.findByEmail(savedMember.getEmail());

		// then
		assertThat(findMember.getId()).isEqualTo(savedMember.getId());

	}

	@Test
	@DisplayName("ID를 통해서 멤버 가져오기")
	void ID를_통해서_Member_불러오기() {
		// given 멤버 저장
		Member savedMember = memberRepository.save(MEMBER());

		// when 실행
		Member findMember = memberService.findById(savedMember.getId());

		// then
		assertThat(findMember.getId()).isEqualTo(savedMember.getId());
	}

	@Test
	@DisplayName("Member의 nickname 중복 여부 검사")
	void nickname_중복검사() {
		// given 멤버 저장
		Member savedMember = memberRepository.save(MEMBER());

		// then 실행
		assertTrue(memberService.isDuplicatedNickname(MEMBER_SERVICE_DTO()));
	}

	@Test
	@DisplayName("Member의 업데이트가 잘 이루어지는가")
	void member_업데이트() {
		// given 멤버 저장
		Member savedMember = memberRepository.save(MEMBER());
		MemberServiceDto memberServiceDto = UPDATE_MEMBER_SERVICE_DTO();

		// when 멤버 업데이트
		memberService.update(UPDATE_MEMBER_SERVICE_DTO());

		Member findMember = memberService.findByEmail(memberServiceDto.getEmail());
		// then 업데이트 됐는지 확인

		assertThat(findMember.getNickname()).isEqualTo(memberServiceDto.getNickname());
		assertThat(findMember.getImageUrl()).isEqualTo(memberServiceDto.getImageUrl());
	}
}
