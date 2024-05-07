package com.example.memetory.domain.memes.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.List;


@Getter
@NoArgsConstructor
@Schema(description = "밈스 전체 조회 반환 포맷")
public class MemesListResponse {

    @Schema(description = "밈스 리스트")
    private List<MemesResponse> memesResponseList;

    private Sort sort;

    private int currentPage;

    private int size;

    private boolean first;

    private boolean last;

    @Builder
    public MemesListResponse(Slice<MemesResponse> memesSlice) {
        this.memesResponseList = memesSlice.getContent();
        this.sort = memesSlice.getSort();
        this.currentPage = memesSlice.getNumber();
        this.size = memesSlice.getSize();
        this.first = memesSlice.isFirst();
        this.last = memesSlice.isLast();
    }
}
