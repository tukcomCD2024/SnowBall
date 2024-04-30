package com.example.memetory.domain.meme.repository;

import java.util.List;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.dto.MemeResponse;

public interface MemeQueryRepository {
	List<MemeResponse> findAllByMember(Member member);
}
