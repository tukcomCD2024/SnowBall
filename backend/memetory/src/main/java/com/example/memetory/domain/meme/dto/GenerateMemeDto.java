package com.example.memetory.domain.meme.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenerateMemeDto {
	@SerializedName("source_image")
	private String sourceImage;
	@SerializedName("target_image")
	private String targetImage;
	@SerializedName("name")
	private String text;
}
