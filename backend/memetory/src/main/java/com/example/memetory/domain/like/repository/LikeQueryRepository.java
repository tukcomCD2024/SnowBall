package com.example.memetory.domain.like.repository;

import java.util.Optional;

import com.example.memetory.domain.like.entity.Like;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.memes.entity.Memes;

public interface LikeQueryRepository {
	Optional<Like> findLikeByMemberAndMemes(Member member, Memes memes);
}
