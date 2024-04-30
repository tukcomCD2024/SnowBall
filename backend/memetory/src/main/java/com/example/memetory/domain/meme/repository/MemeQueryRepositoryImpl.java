package com.example.memetory.domain.meme.repository;

import static com.example.memetory.domain.meme.entity.QMeme.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.dto.MemeResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemeQueryRepositoryImpl implements MemeQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;
	private final MemeQDtoFactory memeQDtoFactory;

	@Override
	public List<MemeResponse> findAllByMember(Member member) {
		return jpaQueryFactory.select(memeQDtoFactory.qMemeResponse())
			.from(meme)
			.where(meme.member.eq(member))
			.fetch();
	}
}
