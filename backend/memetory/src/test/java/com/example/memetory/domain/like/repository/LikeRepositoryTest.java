package com.example.memetory.domain.like.repository;

import static com.example.memetory.domain.like.LikeFixture.*;
import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.memetory.domain.like.entity.Like;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.repository.MemeRepository;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.repository.MemesRepository;
import com.example.memetory.global.RepositoryTest;

// 단위 테스트냐? 아니면 Repository와 Service 계층을 합친 Service 통합 테스트냐...
// 좋은 테스트란 무엇인가?
// 주먹구구식 테스트를 진행하는 것 보더 어떤 테스트를 진행하는게 좋은지 생각해 볼 것
@DisplayName("like 레포지토리 테스트의 ")
@RepositoryTest
public class LikeRepositoryTest {
	@Autowired
	private LikeRepository likeRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemesRepository memesRepository;
	@Autowired
	private MemeRepository memeRepository;

	@Test
	@DisplayName("Member와 Memes로 Like 찾기")
	void Like_찾기() {
		// given
		Member member = MEMBER();
		memberRepository.save(member);
		Meme meme = FIRST_MEME(member);
		memeRepository.save(meme);
		Memes memes = MEMES(member, meme);
		memesRepository.save(memes);
		Like like = LIKE(member, memes);
		Like expect = likeRepository.save(like);

		// when
		Optional<Like> result = likeRepository.findLikeByMemberAndMemes(member, memes);

		// then
		assertThat(expect).isEqualTo(result.get());
	}
}
