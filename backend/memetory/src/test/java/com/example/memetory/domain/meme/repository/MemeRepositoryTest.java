package com.example.memetory.domain.meme.repository;

import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.global.config.JpaAuditingConfig;

@DataJpaTest
@DisplayName("meme 레포지토리 테스트의 ")
@Import(JpaAuditingConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
		Meme savedMeme = memeRepository.save(FIRST_MEME);

		// then 밈이 저장됐는지 확인
		assertThat(memeRepository.findById(savedMeme.getId()).get()).isEqualTo(savedMeme);
	}

	@Test
	@DisplayName("Member를 통한 전체 Meme조회")
	public void Member_전체_Meme_조회() {
		// given DB에 밈과 유저를 저장
		memberRepository.save(SECOND_MEMBER());

		List<Meme> savedMemes = List.of(FIRST_MEME, SECOND_MEME, ANOTHER_MEMBER_MEME);
		memeRepository.saveAll(savedMemes);

		// when MEMBER를 통해서 MEME을 조회
		List<Meme> findMemes = memeRepository.findAllByMember(savedMember);

		// then 일치하는지 조회
		assertThat(findMemes).isEqualTo(List.of(FIRST_MEME, SECOND_MEME));
	}
}
