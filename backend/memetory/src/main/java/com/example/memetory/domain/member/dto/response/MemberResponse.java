package com.example.memetory.domain.member.dto.response;

import java.time.LocalDateTime;

import com.example.memetory.domain.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberResponse {
	private Long memberId;
	private String nickName;
	private String imageUrl;
	private LocalDateTime createdAt;

	@QueryProjection
	@Builder
	public MemberResponse(Long memberId, String nickName, String imageUrl, LocalDateTime createdAt) {
		this.memberId = memberId;
		this.nickName = nickName;
		this.imageUrl = imageUrl;
		this.createdAt = createdAt;
	}

	public static MemberResponse of(Member member) {
		return MemberResponse.builder()
			.memberId(member.getId())
			.nickName(member.getNickname())
			.imageUrl(member.getImageUrl())
			.createdAt(member.getCreatedAt())
			.build();
	}
}
