package com.example.memetory.domain.memes;

import static com.example.memetory.domain.comment.CommentFixture.*;
import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
import static com.example.memetory.global.response.ErrorCode.*;
import static com.example.memetory.global.response.ResultCode.*;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.example.memetory.domain.comment.dto.CommentInfo;
import com.example.memetory.domain.comment.dto.request.CommentRequest;
import com.example.memetory.domain.comment.entity.Comment;
import com.example.memetory.domain.comment.repository.CommentRepository;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.repository.MemeRepository;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.repository.MemesRepository;
import com.example.memetory.global.integration.BaseIntegrationTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("Comment 통합 테스트의 ")
public class MemesCommentIntegrationTest extends BaseIntegrationTest {
	@Autowired
	MemeRepository memeRepository;
	@Autowired
	MemesRepository memesRepository;
	@Autowired
	CommentRepository commentRepository;

	private Meme meme;
	private Memes memes;

	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();

		meme = memeRepository.save(MEME(member));
		memes = memesRepository.save(MEMES(member, meme));
	}

	@Test
	@DisplayName("memesId와 CommentRequest를 통한 댓글 생성 성공")
	void Given_memesIdAndCommentRequest_When_registerComment_() {
		// given
		CommentRequest request = CommentRequest.builder()
			.content("댓글 생성 완료")
			.build();

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(request)
				.when()
				.post("/memes/{memesId}/comments", memes.getId())
				.then()
				.log()
				.all()
				.extract();

		CommentInfo result = response.jsonPath().getObject("data", CommentInfo.class);

		// then
		assertThat(result.getContent()).isEqualTo(request.getContent());
	}

	@Test
	@DisplayName("접근 불가능한 멤버로 인한 MEMBER_ACCESS_DENY 반환")
	void Given_DeniedMember_When_deleteComment_Then_MEMBER_NOT_DENY() {
		// then
		Member deninedMember = memberRepository.save(OTHER_MEMBER());
		Comment comment = commentRepository.save(COMMENT(deninedMember, memes));

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.delete("/memes/{memesId}/comments/{commentId}", memes.getId(), comment.getId())
				.then()
				.log()
				.all()
				.extract();

		String result = response.jsonPath().get(ERROR_MESSAGE);

		//then
		assertThat(result).isEqualTo(MEMBER_ACCESS_DENY.getMessage());
	}

	@Test
	@DisplayName("commentId 통한 댓글 삭제 성공")
	void Given_commentId_When_deleteComment_Then_DELETE_COMMENT_SUCCESS() {
		// then
		Comment comment = commentRepository.save(COMMENT(member, memes));

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.delete("/memes/{memesId}/comments/{commentId}", memes.getId(), comment.getId())
				.then()
				.log()
				.all()
				.extract();

		String result = response.jsonPath().get(MESSAGE);

		//then
		assertThat(result).isEqualTo(DELETE_COMMENT_SUCCESS.getMessage());
	}
}
