package com.example.memetory.domain.comment.repository;

import static com.example.memetory.domain.comment.entity.QComment.*;
import static com.example.memetory.domain.member.entity.QMember.*;
import static com.example.memetory.domain.memes.entity.QMemes.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.example.memetory.domain.comment.dto.CommentInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepositoryImpl implements CommentQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;
	private final CommentQDtoFactory commentQDtoFactory;

	@Override
	public Slice<CommentInfo> findCommentsSliceByMemesId(Long memesId, Pageable pageable) {
		int pageSize = pageable.getPageSize();
		List<CommentInfo> memesList = jpaQueryFactory.select(commentQDtoFactory.qCommentInfo())
			.from(comment)
			.where(comment.memes.id.eq(memesId))
			.join(comment.member, member)
			.join(comment.memes, memes)
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
}
