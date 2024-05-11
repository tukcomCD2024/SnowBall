package com.example.memetory.domain.memes.dto.response;

import java.time.LocalDateTime;

import com.example.memetory.domain.memes.entity.Memes;
import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "밈스 미리보기 정보")
public class MemesInfo {

	@Schema(description = "밈스 아이디")
	private Long memesId;

	@Schema(description = "밈스를 생성한 멤버 닉네임")
	private String memberNickname;

	@Schema(description = "밈스 제목")
	private String title;

	@Schema(description = "댓글 수")
	private Long commentCount;

	@Schema(description = "좋아요 수")
	private Long likeCount;

	@Schema(description = "밈스 생성 시각")
	private LocalDateTime createdAt;

	@QueryProjection
	@Builder
	public MemesInfo(Long memesId, String memberNickname, String title, Long commentCount, Long likeCount,
		LocalDateTime createdAt) {
		this.memesId = memesId;
		this.memberNickname = memberNickname;
		this.title = title;
		this.commentCount = commentCount;
		this.likeCount = likeCount;
		this.createdAt = createdAt;
	}

	public static MemesInfo of(Memes memes) {
		return MemesInfo.builder()
			.memesId(memes.getId())
			.memberNickname(memes.getMember().getNickname())
			.title(memes.getTitle())
			.commentCount(memes.getCommentCount())
			.likeCount(memes.getLikeCount())
			.createdAt(memes.getCreatedAt())
			.build();
	}
}
