package com.example.memetory.domain.meme.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenerateMemeListRequest {
	private List<GenerateMeme> scene;
}
