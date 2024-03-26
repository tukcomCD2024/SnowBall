package com.example.memetory.domain.memes.entity;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memes_id")
    private Long Id;

    private String title;

    private int likeCount;

    private int commentCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meme_id")
    private Meme meme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Memes(String title, int likeCount, int commentCount, Meme meme, Member member) {
        this.title = title;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.meme = meme;
        this.member = member;
    }

    public void addLikeCount() {
        this.likeCount++;
    }

    public void cancelLikeCount() {
        this.likeCount--;
    }
}
