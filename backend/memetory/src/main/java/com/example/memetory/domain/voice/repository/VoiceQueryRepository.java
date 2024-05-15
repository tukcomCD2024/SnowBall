package com.example.memetory.domain.voice.repository;

import com.example.memetory.domain.voice.entity.Voice;

import java.util.Optional;

public interface VoiceQueryRepository {
    Optional<Voice> findByMemberId(Long memberId);
}
