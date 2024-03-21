package com.example.memetory.domain.meme.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenerateMeme {
	private String sourceImage;
	private String targetImage;
	private String text;
}
