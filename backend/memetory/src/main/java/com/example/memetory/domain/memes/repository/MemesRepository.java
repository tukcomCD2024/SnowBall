package com.example.memetory.domain.memes.repository;

import com.example.memetory.domain.memes.entity.Memes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MemesRepository extends JpaRepository<Memes, Long> {

    @Query("SELECT m FROM Memes m ORDER BY m.likeCount DESC")
    @EntityGraph(attributePaths = {"meme"})
    List<Memes> findTopMemesByLikeCount(Pageable pageable);

    @Query("SELECT m FROM Memes m WHERE m.createdAt >= :oneMonthAgo ORDER BY m.likeCount DESC")
    @EntityGraph(attributePaths = {"meme"})
    List<Memes> findTopMemesByLikeCountForPeriod(Pageable pageable, LocalDateTime oneMonthAgo);
}
