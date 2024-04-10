package com.example.memetory.domain.voice.repository;

import com.example.memetory.domain.voice.entity.Voice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoiceRepository extends JpaRepository<Voice, Long> {
}
