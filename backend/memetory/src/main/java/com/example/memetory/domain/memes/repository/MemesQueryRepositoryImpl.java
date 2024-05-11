package com.example.memetory.domain.memes.repository;

import static com.example.memetory.domain.like.entity.QLike.*;
import static com.example.memetory.domain.member.entity.QMember.*;
import static com.example.memetory.domain.meme.entity.QMeme.*;
import static com.example.memetory.domain.memes.entity.QMemes.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.example.memetory.domain.memes.dto.response.MemesInfo;
import com.example.memetory.domain.memes.entity.Memes;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemesQueryRepositoryImpl implements MemesQueryRepository {
	private final Long TOP_TEN = 10L;

	private final JPAQueryFactory jpaQueryFactory;
	private final MemesQDtoFactory memesQDtoFactory;

	@Override
	public Optional<Memes> findByMemesId(Long memesId) {
		return Optional.ofNullable(jpaQueryFactory.selectFrom(memes)
			.where(memes.id.eq(memesId))
			.join(memes.member, member)
			.join(memes.meme, meme)
			.fetchOne());
	}

	@Override
	public Slice<MemesInfo> findAllMemesSlice(Pageable pageable) {
		int pageSize = pageable.getPageSize();
		List<MemesInfo> memesList = jpaQueryFactory.select(memesQDtoFactory.qMemesInfo())
			.from(memes)
			.join(memes.member, member)
			.offset(pageable.getOffset())
			.limit(pageSize + 1)
			.fetch();

		boolean hasNext = false;
		if (memesList.size() > pageSize) {
			memesList.remove(pageSize);
			hasNext = true;
		}

		return new SliceImpl<>(memesList, pageable, hasNext);
	}

	@Override
	public List<MemesInfo> findTopMemesOrderByLikeCount() {
		return jpaQueryFactory.select(memesQDtoFactory.qMemesInfo())
			.from(memes)
			.join(memes.member, member)
			.orderBy(memes.likeCount.desc())
			.limit(TOP_TEN)
			.fetch();
	}

	@Override
	public List<MemesInfo> findTopMemesByLikeCountForPeriod(LocalDateTime time) {
		return jpaQueryFactory.select(memesQDtoFactory.qMemesInfoSetLike())
			.from(like)
			.join(like.memes, memes)
			.groupBy(memes.id)
			.where(like.createdAt.goe(time))
			.orderBy(memes.count().desc())
			.limit(TOP_TEN)
			.fetch();
	}
}
