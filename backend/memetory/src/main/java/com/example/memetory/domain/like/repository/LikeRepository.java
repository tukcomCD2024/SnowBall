package com.example.memetory.domain.like.repository;

import com.example.memetory.domain.like.entity.Like;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.memes.dto.MemesRankDto;
import com.example.memetory.domain.memes.entity.Memes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeQueryRepository {
}
