package com.example.memetory.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.member.dto.MemberServiceDto;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.exception.NotFoundMemberException;
import com.example.memetory.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
	private final MemberRepository memberRepository;

	@Transactional
	public void register(MemberServiceDto memberServiceDto) {
		Member member = findByEmail(memberServiceDto.getEmail());

		member.update(memberServiceDto);
	}

	@Transactional(readOnly = true)
	public Member findByEmail(String email) {
		return memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);
	}

	@Transactional(readOnly = true)
	public Member findById(Long id) {
		return memberRepository.findById(id).orElseThrow(NotFoundMemberException::new);
	}
}
