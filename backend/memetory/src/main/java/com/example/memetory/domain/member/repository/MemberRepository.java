package com.example.memetory.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.entity.SocialType;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	boolean existsMemberByNickname(String nickname);

	Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
