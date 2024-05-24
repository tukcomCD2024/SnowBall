package com.example.memetory.domain.meme.repository;

import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.domain.meme.dto.MemeResponse;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.global.RepositoryTest;

@DisplayName("meme 레포지토리 테스트의 ")
@RepositoryTest
public class MemeRepositoryTest {
	@Autowired
	private MemeRepository memeRepository;
	@Autowired
	private MemberRepository memberRepository;

	private Member member;

	@BeforeEach
	void setUp() {
		member = memberRepository.save(MEMBER());
	}

	@Test
	@DisplayName("id를 통한 밈 반환 성공")
	public void Given_id_Then_findById_Return_Meme() {
		// given
		Meme savedMeme = memeRepository.save(MEME(member));

		// when 밈 저장하기
		Meme result = memeRepository.findById(savedMeme.getId()).get();

		// then 밈이 저장됐는지 확인
		assertThat(result).isEqualTo(savedMeme);
	}

	@Test
	@DisplayName("Member를 통한 사용자 Page<MemeResponse> 반환 성공")
		public void Given_Member_Then_findAllByMember_Then_Page_MemesResponse() {
		// given
		final int memeCount = 18;
		final int pageSize = 10;
		Member anthorMember = memberRepository.save(OTHER_MEMBER());
		Pageable pageable = PageRequest.of(0, pageSize);

		for (int i = 0; i < memeCount; i++)
			memeRepository.save(MEME(member));
		for (int i = 0; i < 2; i++)
			memeRepository.save(MEME(anthorMember));

		// when
		Page<MemeResponse> findMemes = memeRepository.findAllByMember(member, pageable);

		// then
		assertThat(findMemes.getContent()).hasSize(pageSize);
		assertThat(findMemes.getTotalElements()).isEqualTo(memeCount);
	}
}
