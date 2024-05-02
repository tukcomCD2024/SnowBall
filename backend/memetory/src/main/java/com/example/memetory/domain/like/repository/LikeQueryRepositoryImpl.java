package com.example.memetory.domain.like.repository;

import static com.example.memetory.domain.like.entity.QLike.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.memetory.domain.like.entity.Like;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.memes.entity.Memes;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LikeQueryRepositoryImpl implements LikeQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<Like> findLikeByMemberAndMemes(Member member, Memes memes) {
		return Optional.ofNullable(jpaQueryFactory
			.selectFrom(like)
			.where(like.member.eq(member)
				.and(like.memes.eq(memes)))
			.fetchFirst());
	}
}
