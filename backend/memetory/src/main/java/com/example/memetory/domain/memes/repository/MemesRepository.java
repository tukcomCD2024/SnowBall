package com.example.memetory.domain.memes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.memetory.domain.memes.entity.Memes;

public interface MemesRepository extends JpaRepository<Memes, Long>, MemesQueryRepository {
}
