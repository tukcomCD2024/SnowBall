package com.example.memetory.domain.meme.service;

import static com.example.memetory.domain.member.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.domain.member.service.MemberService;
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
}
