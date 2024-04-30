package com.example.memetory.domain.meme.repository;

import static com.example.memetory.domain.meme.entity.QMeme.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
	public Page<MemeResponse> findAllByMember(Member member, Pageable pageable) {
		List<MemeResponse> content = jpaQueryFactory.select(memeQDtoFactory.qMemeResponse())
			.from(meme)
			.where(meme.member.eq(member))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long count = jpaQueryFactory.select(meme.count())
			.from(meme)
			.where(meme.member.eq(member))
			.fetchOne();

		return new PageImpl<>(content, pageable, count);
	}
}
