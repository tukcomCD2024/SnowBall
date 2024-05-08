package com.example.memetory.domain.memes.repository;

import com.example.memetory.domain.memes.entity.Memes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemesRepository extends JpaRepository<Memes, Long> {

    @Query("SELECT ms FROM Memes ms WHERE ms.Id = :memesId")
    @EntityGraph(attributePaths = {"meme", "member"})
    Optional<Memes> findByMemesId(Long memesId);

    @EntityGraph(attributePaths = {"meme", "member"})
    Slice<Memes> findMemesBy(Pageable pageable);

    @Query("SELECT ms FROM Memes ms ORDER BY ms.likeCount DESC")
    @EntityGraph(attributePaths = {"meme"})
    List<Memes> findTopMemesByLikeCount(Pageable pageable);

    @Query("SELECT ms FROM Memes ms WHERE ms.createdAt >= :time ORDER BY ms.likeCount DESC")
    @EntityGraph(attributePaths = {"meme"})
    List<Memes> findTopMemesByLikeCountForPeriod(Pageable pageable, LocalDateTime time);
}
