package com.example.memetory.domain.member.dto;

import java.time.LocalDateTime;

import com.example.memetory.domain.member.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberResponse {
	private Long memberId;
	private String nickName;
	private LocalDateTime createdAt;

	@Builder
	public MemberResponse(Long memberId, String nickName, LocalDateTime createdAt) {
		this.memberId = memberId;
		this.nickName = nickName;
		this.createdAt = createdAt;
	}

	public static MemberResponse of(Member member) {
		return MemberResponse.builder()
			.memberId(member.getId())
			.nickName(member.getNickname())
			.createdAt(member.getCreatedAt())
			.build();
	}
}
