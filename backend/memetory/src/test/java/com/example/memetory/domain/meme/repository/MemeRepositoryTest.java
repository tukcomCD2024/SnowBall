package com.example.memetory.domain.meme.repository;

import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

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

	private Member savedMember;

	@BeforeEach
	void setUp() {
		savedMember = memberRepository.save(MEMBER());
	}

	@Test
	@DisplayName("Meme 저장 확인과 Meme Id를 통한 조회 확인")
	public void Meme_저장() {
		// when 밈 저장하기
		Meme savedMeme = memeRepository.save(FIRST_MEME(savedMember));

		// then 밈이 저장됐는지 확인
		assertThat(memeRepository.findById(savedMeme.getId()).get()).isEqualTo(savedMeme);
	}

	@Test
	@DisplayName("Member를 통한 사용자 Meme 전체 조회")
	public void 전체_Meme_조회() {
		// given DB에 밈과 유저를 저장
		Member anthorMember = memberRepository.save(SECOND_MEMBER());
		Meme firstMeme = FIRST_MEME(savedMember);
		Meme secondtMeme = SECOND_MEME(savedMember);
		Meme anotherMemberMeme = FIRST_MEME(anthorMember);
		Pageable pageable = PageRequest.of(0, 10);

		List<Meme> savedMemes = List.of(firstMeme, secondtMeme, anotherMemberMeme);
		memeRepository.saveAll(savedMemes);

		// when MEMBER를 통해서 MEME을 조회
		Page<MemeResponse> findMemes = memeRepository.findAllByMember(savedMember, pageable);

		// then 일치하는지 조회
		assertThat(findMemes.getContent()).usingRecursiveComparison()
			.isEqualTo(List.of(firstMeme, secondtMeme).stream().map(MemeResponse::of).toList());
	}
}
