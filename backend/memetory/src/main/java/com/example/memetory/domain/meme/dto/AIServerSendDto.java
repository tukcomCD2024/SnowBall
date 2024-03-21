package com.example.memetory.domain.meme.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AIServerSendDto {
	@SerializedName("member_id")
	private String memberId;
	@SerializedName("scene")
	private List<GenerateMemeDto> scene;

	@Builder
	public AIServerSendDto(String memberId, List<GenerateMemeDto> scene) {
		this.memberId = memberId;
		this.scene = scene;
	}
}

