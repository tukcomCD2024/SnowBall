package com.example.memetory.domain.memes.event;

import java.time.Year;
import java.util.List;

import com.example.memetory.domain.memes.dto.MemesRankDto;

public record WeeklyRankingCreatedEvent(Year year, int week, List<MemesRankDto> memesRank) {
}
