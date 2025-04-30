package com.example.memetory.domain.memes.event;

import java.time.YearMonth;
import java.util.List;

import com.example.memetory.domain.memes.dto.MemesRankDto;

public record MonthlyRankingCreatedEvent(YearMonth yearMonth, List<MemesRankDto> memesRankDtoList) {

}
