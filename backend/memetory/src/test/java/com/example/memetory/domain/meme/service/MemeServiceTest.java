package com.example.memetory.domain.meme.service;

import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.dto.MemeListResponse;
import com.example.memetory.domain.meme.dto.MemeResponse;
import com.example.memetory.domain.meme.dto.MemeServiceDto;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.repository.MemeRepository;

import jakarta.transaction.Transactional;

@DisplayName("memeService 테스트의 ")
@Transactional
@SpringBootTest
public class MemeServiceTest {
	@Autowired
	private MemeRepository memeRepository;
	@Autowired
	private MemeService memeService;
	@Autowired
	private MemberRepository memberRepository;

	@Mock
	private MemberService memberService;

	private Member savedMember;

	@BeforeEach
	void setUp() {
		savedMember = memberRepository.save(MEMBER());
	}

	@Test
	@DisplayName("밈 저장 성공")
	void 밈_저장() {
		// given Meme을 세팅
		given(memberService.findById(savedMember.getId())).willReturn(savedMember);

		MemeServiceDto memeServiceDto = MemeServiceDto.builder()
			.memberId(savedMember.getId())
			.s3Url("myS3Url")
			.build();

		// when
		memeService.register(memeServiceDto);

		// then
		assertThat(memeRepository.findAllByMember(savedMember).get(0).getMember()).isEqualTo(savedMember);
	}

	@Test
	@DisplayName("Meme ID를 통해 MemeResponse를 반환")
	void 단일_밈_조회() {
		// given 밈 저장, memServiceDto, memeReponse 생성
		Meme savedMeme = memeRepository.save(FIRST_MEME(savedMember));
		MemeServiceDto serviceDto = MemeServiceDto.builder()
			.memeId(savedMeme.getId())
			.build();
		MemeResponse expectedResponse = MemeResponse.of(savedMeme);

		// when
		MemeResponse memeResponse = memeService.getMeme(serviceDto);

		// given
		assertThat(memeResponse.getMemeId()).isEqualTo(expectedResponse.getMemeId());
		assertThat(memeResponse.getS3Url()).isEqualTo(expectedResponse.getS3Url());
	}

	@Test
	@DisplayName("Member를 통한 해당 멤버의 전체 밈 조회")
	void 전체_밈_조회() {
		// given 밈들 저장, MemeListResponse 세팅
		given(memberService.findById(savedMember.getId())).willReturn(savedMember);
		MemeServiceDto memeServiceDto = MemeServiceDto.builder()
			.email(savedMember.getEmail())
			.build();

		List<Meme> memeList = List.of(memeRepository.save(FIRST_MEME(savedMember)),
			memeRepository.save(SECOND_MEME(savedMember)));
		MemeListResponse expectedResponse = MemeListResponse.builder()
			.memeList(memeList.stream().map(MemeResponse::of).toList())
			.build();

		// when
		MemeListResponse response = memeService.getAllMeme(memeServiceDto);

		// then
		assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
	}
}
