package com.example.memetory.domain.comment.service;

import static com.example.memetory.domain.comment.CommentFixture.*;
import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.memetory.domain.comment.dto.CommentInfo;
import com.example.memetory.domain.comment.dto.request.CommentRequest;
import com.example.memetory.domain.comment.entity.Comment;
import com.example.memetory.domain.comment.repository.CommentRepository;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.exception.DeniedAccessException;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.service.MemesService;

@DisplayName("Comment 서비스 테스트의 ")
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
	@InjectMocks
	private CommentService commentService;
	@Mock
	private CommentRepository commentRepository;
	@Mock
	private MemberService memberService;
	@Mock
	private MemesService memesService;

	private Member member;
	private Memes memes;
	private Comment comment;

	@BeforeEach
	void setUp() {
		member = MEMBER();
		Meme meme = MEME(member);
		memes = MEMES(member, meme);
		comment = COMMENT(member, memes);
	}

	@Test
	@DisplayName("CommentRequest를 통한 댓글 생성 성공")
	void Given_CommentRequest_When_saveComment_Then_CommentInfo() {
		// given
		CommentRequest request = COMMENT_REQUEST();

		given(memberService.findMemberFromEmail(request.getEmail())).willReturn(member);
		given(memesService.findMemesFromMemesId(request.getMemesId())).willReturn(memes);
		given(commentRepository.save(any(Comment.class))).willReturn(comment);

		// when
		CommentInfo result = commentService.saveComment(request);

		// then
		assertThat(result.getContent()).isEqualTo(request.getContent());
	}

	@Test
	@DisplayName("접근 권한이 없는 유저로 인한 DeniedAccessException 반환")
	void Given_AccessDeniedMember_When_deleteComment_Throw_AccessDeniedException() {
		// given
		String email = "junrain@ourservice.com";
		Long commentId = -1L;

		given(commentRepository.findById(commentId)).willReturn(Optional.ofNullable(comment));
		given(memberService.findMemberFromEmail(email)).willReturn(OTHER_MEMBER());
		doThrow(new DeniedAccessException()).when(memberService).certifyMember(any(Member.class), any(Member
			.class));

		// then
		assertThrows(DeniedAccessException.class, () -> commentService.deleteComment(email, commentId));
	}

	@Test
	@DisplayName("email과 commentId를 통한 삭제 성공")
	void Given_emailAndCommentId_When_deleteComment_Verify_deleteComment() {
		// given
		String email = "junrain@ourservice.com";
		Long commentId = -1L;

		given(commentRepository.findById(commentId)).willReturn(Optional.ofNullable(comment));
		given(memberService.findMemberFromEmail(email)).willReturn(member);
		doNothing().when(memberService).certifyMember(any(Member.class), any(Member.class));

		// when
		commentService.deleteComment(email, commentId);

		// then
		verify(commentRepository).delete(comment);
	}
}
