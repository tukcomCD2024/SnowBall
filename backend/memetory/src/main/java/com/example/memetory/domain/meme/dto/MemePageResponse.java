package com.example.memetory.domain.meme.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "밈 리스트 반환 포맷")
public class MemePageResponse {
	@Schema(description = "전체 페이지")
	private int totalPage;
	@Schema(description = "현재 페이지")
	private int currentPage;

	@Schema(description = "밈 리스트")
	private List<MemeResponse> memeList;

	@Builder
	public MemePageResponse(int totalPage, int currentPage, List<MemeResponse> memeList) {
		this.totalPage = totalPage;
		this.currentPage = currentPage;
		this.memeList = memeList;
	}
}
