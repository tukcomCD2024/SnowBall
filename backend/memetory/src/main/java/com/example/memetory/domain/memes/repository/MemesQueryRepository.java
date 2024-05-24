package com.example.memetory.domain.memes.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.example.memetory.domain.memes.dto.response.MemesInfoResponse;
import com.example.memetory.domain.memes.entity.Memes;

public interface MemesQueryRepository {
	Optional<Memes> findByMemesId(Long memesId);

	Slice<MemesInfoResponse> findMemesInfoSlice(Pageable pageable);

	List<MemesInfoResponse> findTopMemesOrderByLikeCount();

	List<MemesInfoResponse> findTopMemesOrderByLikeCountForPeriod(LocalDateTime time);
}
