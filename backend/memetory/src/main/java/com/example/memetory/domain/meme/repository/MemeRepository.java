package com.example.memetory.domain.meme.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.memetory.domain.meme.entity.Meme;

public interface MemeRepository extends JpaRepository<Meme, Long> {
}
