package com.example.memetory.domain.meme.dto;

import java.util.List;

import com.example.memetory.domain.meme.entity.Meme;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemeListResponse {
	private List<MemeResponse> memeList;

	@Builder
	public MemeListResponse(List<MemeResponse> memeList) {
		this.memeList = memeList;
	}
}
