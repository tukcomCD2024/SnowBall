package com.example.memetory.domain.memes.service;

import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
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
import com.example.memetory.domain.memes.repository.MemesRepository;

@DisplayName("memesService 테스트의 ")
@ExtendWith(MockitoExtension.class)
public class MemesServiceTest {
	@InjectMocks
	private MemesService memesService;

	@Mock
	private MemberService memberService;
	@Mock
	private MemesRepository memesRepository;
	@Mock
	private MemeService memeService;

	@Test
	@DisplayName("밈스 저장 성공")
	void 밈스_저장() {
		// given
		Member member = MEMBER();
		Meme meme = FIRST_MEME(member);
		MemesServiceDto memesServiceDto = MEMES_SERVICE_DTO();

		// when
		when(memberService.findByEmail(memesServiceDto.getEmail())).thenReturn(member);
		when(memeService.getMemeBetweenService(memesServiceDto.getMemeId())).thenReturn(meme);

		memesService.register(memesServiceDto);

		// then
		verify(memesRepository).save(any());
	}
}
