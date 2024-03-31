package com.example.memetory.domain.voice.entity;

import com.example.memetory.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Voice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voice_id")
    private Long id;

    @Column(name = "s3_url")
    private String s3Url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Voice(String s3Url, Member member) {
        this.s3Url = s3Url;
        this.member = member;
    }
}
