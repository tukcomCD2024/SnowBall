package com.example.memetory.domain.memes.event;

import java.time.LocalDate;
import java.util.List;

import com.example.memetory.domain.memes.dto.MemesRankDto;

public record DailyRankingCreatedEvent(LocalDate date, List<MemesRankDto> memesRank) {
}
