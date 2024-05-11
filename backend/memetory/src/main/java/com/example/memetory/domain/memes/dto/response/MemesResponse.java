package com.example.memetory.domain.memes.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.memetory.domain.comment.dto.CommentInfo;
import com.example.memetory.domain.member.dto.MemberResponse;
import com.example.memetory.domain.memes.entity.Memes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "밈스 상세 정보")
public class MemesResponse {
	@Schema(description = "밈스 아이디")
	private Long memesId;

	@Schema(description = "밈스를 생성한 멤버 정보")
	private MemberResponse member;

	@Schema(description = "밈스로 보여줄 밈의 S3 주소")
	private String memeUrl;

	@Schema(description = "밈스 제목")
	private String title;

	@Schema(description = "댓글 수")
	private Long commentCount;

	@Schema(description = "좋아요 수")
	private Long likeCount;

	@Schema(description = "밈스 생성 시각")
	private LocalDateTime createdAt;

	@Builder
	public MemesResponse(Long memesId, MemberResponse member, String memeUrl, String title, Long commentCount,
		Long likeCount, LocalDateTime createdAt) {
		this.memesId = memesId;
		this.member = member;
		this.memeUrl = memeUrl;
		this.title = title;
		this.commentCount = commentCount;
		this.likeCount = likeCount;
		this.createdAt = createdAt;
	}

	public static MemesResponse of(Memes memes) {
		return MemesResponse.builder()
			.memesId(memes.getId())
			.member(MemberResponse.of(memes.getMember()))
			.memeUrl(memes.getMeme().getS3Url())
			.title(memes.getTitle())
			.commentCount(memes.getCommentCount())
			.likeCount(memes.getLikeCount())
			.createdAt(memes.getCreatedAt())
			.build();
	}
}
