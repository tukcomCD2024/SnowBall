package com.example.memetory.domain.memes.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.example.memetory.domain.memes.dto.response.MemesInfo;
import com.example.memetory.domain.memes.entity.Memes;

public interface MemesQueryRepository {
	Optional<Memes> findByMemesId(Long memesId);

	Slice<MemesInfo> findAllMemesSlice(Pageable pageable);

	List<MemesInfo> findTopMemesOrderByLikeCount();

	List<MemesInfo> findTopMemesByLikeCountForPeriod(LocalDateTime time);
}
