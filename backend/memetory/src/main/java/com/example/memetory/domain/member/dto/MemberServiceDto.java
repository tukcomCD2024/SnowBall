package com.example.memetory.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberServiceDto {
	private String email;
	private String nickname;
	private String imageUrl;
}
