package com.example.memetory.domain.meme.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShotStackCallBackRequest {
	private String status;
	private String url;
	private String error;
}
