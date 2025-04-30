package com.example.memetory.domain.memes.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.memetory.domain.memes.entity.Memes;

public interface MemesRepository extends JpaRepository<Memes, Long>, MemesQueryRepository {
	List<Memes> findMemesByIdIn(Collection<Long> ids);
}
