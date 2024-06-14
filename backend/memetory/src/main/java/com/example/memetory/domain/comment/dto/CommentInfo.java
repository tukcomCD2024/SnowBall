package com.example.memetory.domain.comment.dto;

import java.time.LocalDateTime;

import com.example.memetory.domain.comment.entity.Comment;
import com.example.memetory.domain.member.dto.response.MemberResponse;
import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "댓글 정보")
public class CommentInfo {

	@Schema(description = "댓글 아이디")
	private Long commentId;

	@Schema(description = "댓글 쓴 멤버 정보")
	private MemberResponse member;

	@Schema(description = "댓글 내용")
	private String content;

	@Schema(description = "댓글 생성 시각")
	private LocalDateTime createdAt;

	@QueryProjection
	@Builder
	public CommentInfo(Long commentId, MemberResponse member, String content, LocalDateTime createdAt) {
		this.commentId = commentId;
		this.member = member;
		this.content = content;
		this.createdAt = createdAt;
	}

	public static CommentInfo of(Comment comment) {
		return CommentInfo.builder()
			.commentId(comment.getId())
			.member(MemberResponse.of(comment.getMember()))
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.build();
	}
}
