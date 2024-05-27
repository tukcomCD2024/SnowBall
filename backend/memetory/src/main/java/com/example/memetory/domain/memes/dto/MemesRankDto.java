package com.example.memetory.domain.memes.dto;

import org.springframework.data.redis.core.ZSetOperations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemesRankDto {
	private Long memesId;
	private Long score;

	public static MemesRankDto of(ZSetOperations.TypedTuple<Long> zSet) {
		return MemesRankDto.builder()
			.memesId(zSet.getValue())
			.score(zSet.getScore().longValue())
			.build();
	}
}
