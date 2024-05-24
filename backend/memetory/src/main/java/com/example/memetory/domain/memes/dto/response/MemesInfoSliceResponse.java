package com.example.memetory.domain.memes.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "밈스 조회 무한스크롤 반환 포맷")
public class MemesInfoSliceResponse {
	@Schema(description = "현재 페이지")
	private int currentPage;
	@Schema(description = "다음 페이지 여부")
	private boolean hasNext;

	@Schema(description = "밈스 리스트")
	private List<MemesInfoResponse> memesInfoResponseList;

	@Builder
	public MemesInfoSliceResponse(int currentPage, boolean hasNext, List<MemesInfoResponse> memesInfoResponseList) {
		this.currentPage = currentPage;
		this.hasNext = hasNext;
		this.memesInfoResponseList = memesInfoResponseList;
	}
}
