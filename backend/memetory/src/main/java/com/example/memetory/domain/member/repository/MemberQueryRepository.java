package com.example.memetory.domain.member.repository;

import java.util.Optional;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.entity.SocialType;

public interface MemberQueryRepository {
	Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

	Optional<Member> findByEmail(String email);

	boolean existMemberByNickname(String email);
}
