package com.example.memetory.domain.voice.repository;

import com.example.memetory.domain.voice.entity.Voice;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.example.memetory.domain.member.entity.QMember.member;
import static com.example.memetory.domain.voice.entity.QVoice.voice;

@Repository
@RequiredArgsConstructor
public class VoiceQueryRepositoryImpl implements VoiceQueryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Voice> findByMemberId(Long memberId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(voice)
                .where(voice.member.id.eq(memberId))
                .join(voice.member, member)
                .fetchOne());
    }
}
