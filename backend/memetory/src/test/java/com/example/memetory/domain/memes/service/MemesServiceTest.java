package com.example.memetory.domain.memes.service;

import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.service.MemeService;
import com.example.memetory.domain.memes.dto.MemesServiceDto;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.repository.MemesRepository;

@DisplayName("memesService 테스트의 ")
@ExtendWith(MockitoExtension.class)
public class memesServiceTest {
	@Mock
	private MemesRepository memesRepository;
	@Mock
	private MemeService memeService;
	@Mock
	private MemberService memberService;

	@InjectMocks
	private MemesService memesService;

	@Test
	@DisplayName("밈스 저장 성공")
	void 밈스_저장() {
		// given
		Member member = MEMBER();
		Meme meme = FIRST_MEME(member);
		MemesServiceDto request = MEMES_SERVICE_DTO();
		Memes memes = request.toEntity(member, meme);

		// when
		when(memberService.findByEmail(request.getEmail())).thenReturn(member);
		when(memeService.getMemeBetweenService(request.getMemeId())).thenReturn(meme);
		when(memesRepository.save(any())).thenReturn(memes);

		Memes result = memesService.register(request);

		// then
		assertThat(result).isEqualTo(memes);
	}
}
