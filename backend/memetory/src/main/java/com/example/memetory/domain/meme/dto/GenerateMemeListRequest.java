package com.example.memetory.domain.meme.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenerateMemeListRequest {
	private List<GenerateMemeDto> scene;

	public MemeServiceDto toServiceDto(String email) {
		return MemeServiceDto.builder()
			.email(email)
			.scene(scene)
			.build();
	}
}
