package com.example.memetory.domain.meme;

import java.util.List;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.dto.AIServerSendDto;
import com.example.memetory.domain.meme.dto.GenerateMemeDto;
import com.example.memetory.domain.meme.dto.GenerateMemeListRequest;
import com.example.memetory.domain.meme.dto.MemeServiceDto;
import com.example.memetory.domain.meme.entity.Meme;

public class MemeFixture {
	public static Meme MEME(Member member) {
		return Meme.builder().s3Url("s3Url").member(member).build();
	}

	public static Meme OTHER_MEME(Member member) {
		return Meme.builder().s3Url("s3Url2").member(member).build();
	}

	public static MemeServiceDto MEME_SERVICE_DTO() {
		return MemeServiceDto.builder()
			.memberId(-1L)
			.memeId(-1L)
			.s3Url("myS3Url")
			.build();
	}

	public static GenerateMemeListRequest GENERATE_MEME_LIST_REQUEST() {
		List<GenerateMemeDto> scenes = List.of(
			new GenerateMemeDto("sourceImage1", "1", "Hello", "1"),
			new GenerateMemeDto("sourceImage2", "2", "Hello", "2"),
			new GenerateMemeDto("sourceImage3", "3", "Hello", "3")
		);

		return new GenerateMemeListRequest(scenes);
	}

	public static AIServerSendDto AI_SERVER_SEND_DTO() {
		return new AIServerSendDto("1", GENERATE_MEME_LIST_REQUEST().getScene());
	}
}
