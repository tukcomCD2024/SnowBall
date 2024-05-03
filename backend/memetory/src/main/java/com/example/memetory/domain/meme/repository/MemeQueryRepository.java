package com.example.memetory.domain.meme.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.dto.MemeResponse;

public interface MemeQueryRepository {
	Page<MemeResponse> findAllByMember(Member member, Pageable pageable);
}
