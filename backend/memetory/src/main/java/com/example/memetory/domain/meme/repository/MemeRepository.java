package com.example.memetory.domain.meme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.entity.Meme;

public interface MemeRepository extends JpaRepository<Meme, Long> {
	List<Meme> findAllByMember(Member member);
}
