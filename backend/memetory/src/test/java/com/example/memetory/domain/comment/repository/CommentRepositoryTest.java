package com.example.memetory.domain.comment.repository;

import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.example.memetory.domain.comment.dto.CommentInfo;
import com.example.memetory.domain.comment.entity.Comment;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.repository.MemeRepository;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.repository.MemesRepository;
import com.example.memetory.global.RepositoryTest;

@DisplayName("Comment 레포지토리 테스트의 ")
@RepositoryTest
public class CommentRepositoryTest {
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemesRepository memesRepository;
	@Autowired
	private MemeRepository memeRepository;

	@Test
	@DisplayName("memesId를 통한 Slice<CommentInfo> 반환")
	void Given_memesId_When_findCommentsSliceByMemesId_Then_Slice_CommentInfo() {
		// given
		Member member = memberRepository.save(MEMBER());
		Meme meme = memeRepository.save(MEME(member));
		Memes memes = memesRepository.save(MEMES(member, meme));
		for (int i = 0; i < 30; i++) {
			commentRepository.save(new Comment("댓글" + i, member, memes));
		}

		Pageable pageable = PageRequest.of(0, 10);

		// when
		Slice<CommentInfo> result = commentRepository.findCommentsSliceByMemesId(memes.getId(), pageable);

		// then
		assertThat(result).hasSize(10);
		assertTrue(result.hasNext());

		Long memberResponseId = result.getContent().get(0).getMember().getMemberId();
		assertThat(memberResponseId).isEqualTo(member.getId());
	}
}
