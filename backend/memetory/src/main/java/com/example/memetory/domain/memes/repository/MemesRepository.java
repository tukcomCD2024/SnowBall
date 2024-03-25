package com.example.memetory.domain.memes.repository;

import com.example.memetory.domain.memes.entity.Memes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemesRepository extends JpaRepository<Memes, Long> {
}
