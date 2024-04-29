package com.example.memetory.domain.member.repository;

import static com.example.memetory.domain.member.entity.QMember.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.entity.SocialType;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId) {
		return Optional.ofNullable(jpaQueryFactory.selectFrom(member)
			.where(member.socialType.eq(socialType).and(member.socialId.eq(socialId)))
			.fetchOne());
	}

	@Override
	public Optional<Member> findByEmail(String email) {
		return Optional.ofNullable(jpaQueryFactory.selectFrom(member).where(member.email.eq(email)).fetchOne());
	}

	@Override
	public boolean existsMemberByNickname(String nickname) {
		return jpaQueryFactory.selectFrom(member).where(member.nickname.eq(nickname)).fetchOne() != null;
	}
}
