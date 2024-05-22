package com.example.memetory.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.member.dto.MemberServiceDto;
import com.example.memetory.domain.member.dto.response.MemberResponse;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.exception.DuplicatedMemberException;
import com.example.memetory.domain.member.exception.NotFoundMemberException;
import com.example.memetory.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	@Transactional
	public MemberResponse updateMember(MemberServiceDto memberServiceDto) {
		if (isDuplicateNickname(memberServiceDto.getNickname())) {
			throw new DuplicatedMemberException();
		}
		Member member = findMemberFromEmail(memberServiceDto.getEmail());
		member.update(memberServiceDto);

		return MemberResponse.of(member);
	}

	private boolean isDuplicateNickname(String nickname) {
		return memberRepository.existMemberByNickname(nickname);
	}

	@Transactional(readOnly = true)
	public Member findMemberFromEmail(String email) {
		return memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);
	}

	@Transactional(readOnly = true)
	public MemberResponse findMemberResponse(MemberServiceDto memberServiceDto) {
		Member member = findMemberFromEmail(memberServiceDto.getEmail());

		return MemberResponse.of(member);
	}

	@Transactional(readOnly = true)
	public Member findMemberFromId(Long id) {
		return memberRepository.findById(id).orElseThrow(NotFoundMemberException::new);
	}
}
