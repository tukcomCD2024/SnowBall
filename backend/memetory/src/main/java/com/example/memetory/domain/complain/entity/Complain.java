package com.example.memetory.domain.complain.entity;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Complain extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complain_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memes_id")
    private Memes memes;

    @Builder
    public Complain(String content, Member member, Memes memes) {
        this.content = content;
        this.member = member;
        this.memes = memes;
    }
}
