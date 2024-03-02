package com.example.memetory.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.entity.SocialType;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	Optional<Member> findByNickname(String nickname);

	/**
	 * 소셜 타입과 소셜의 식별값으로 회원 찾는 메소드
	 * 추가 정보를 입력받아 회원 가입을 진행할 때 소셜 타입, 식별자로 해당 회원을 찾기 위한 메소드
	 */
	Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
