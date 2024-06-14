package com.example.memetory.domain.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.example.memetory.domain.comment.dto.CommentInfo;

public interface CommentQueryRepository {
	Slice<CommentInfo> findCommentsSliceByMemesId(Long memesId, Pageable pageable);
}
