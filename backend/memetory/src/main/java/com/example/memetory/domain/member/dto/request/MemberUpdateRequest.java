package com.example.memetory.domain.member.dto.request;

import com.example.memetory.domain.member.dto.MemberServiceDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Schema(description = "멤버 업데이트 포맷")
public class MemberUpdateRequest {
	@Schema(description = "변경할 닉네임")
	private String nickname;
	@Schema(description = "변경할 이미지 S3 Url")
	private String imageUrl;

	public MemberServiceDto toServiceDto(String email) {
		return MemberServiceDto.builder()
			.email(email)
			.nickname(this.nickname)
			.imageUrl(this.imageUrl)
			.build();
	}
}
