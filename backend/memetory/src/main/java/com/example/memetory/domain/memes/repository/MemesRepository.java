package com.example.memetory.domain.memes.repository;

import com.example.memetory.domain.memes.entity.Memes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemesRepository extends JpaRepository<Memes, Long> {

    @Query(value = "select * from memes ORDER BY like_count desc limit 10", nativeQuery = true)
    List<Memes> findTopTenMemesByLikeCount();
}
