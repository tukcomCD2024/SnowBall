package com.example.memetory.domain.meme.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AIServerSendDto {
	private String memberId;
	private List<GenerateMemeDto> scene;

	@Builder
	public AIServerSendDto(String memberId, List<GenerateMemeDto> scene) {
		this.memberId = memberId;
		this.scene = scene;
	}
}

