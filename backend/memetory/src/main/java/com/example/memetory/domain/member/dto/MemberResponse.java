package com.example.memetory.domain.member.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberResponse {
	private String memberId;
	private String nickName;
	private LocalDateTime createdAt;

	@Builder
	public MemberResponse(String memberId, String nickName, LocalDateTime createdAt) {
		this.memberId = memberId;
		this.nickName = nickName;
		this.createdAt = createdAt;
	}
}
