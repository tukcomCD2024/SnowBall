package com.example.memetory.domain.memes;

import static com.example.memetory.domain.like.LikeFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
import static com.example.memetory.global.response.ErrorCode.*;
import static com.example.memetory.global.response.ResultCode.*;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ZSetOperations;

import com.example.memetory.domain.like.entity.Like;
import com.example.memetory.domain.like.repository.LikeRepository;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.repository.MemeRepository;
import com.example.memetory.domain.memes.dto.response.MemesInfoResponse;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.repository.MemesRepository;
import com.example.memetory.global.integration.BaseIntegrationTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("MemesLike 통합 테스트의 ")
public class MemesLikeIntegrationTest extends BaseIntegrationTest {
	@Autowired
	MemeRepository memeRepository;
	@Autowired
	MemesRepository memesRepository;
	@Autowired
	LikeRepository likeRepository;

	@Autowired
	private ZSetOperations<String, Long> rankingZSet;
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	private Meme meme;
	private Memes memes;

	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();
		redisConnectionFactory.getConnection().flushAll();

		meme = memeRepository.save(MEME(member));
		memes = memesRepository.save(MEMES(member, meme));
	}

	@Test
	@DisplayName("memesId를 통한 좋아요 등록 성공")
	void Given_memesId_When_registerLike_Then_CREATE_LIKE_SUCCESS() {
		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.post("/memes/{memesId}/like", memes.getId())
				.then()
				.log()
				.all()
				.extract();

		String result = response.jsonPath().get(MESSAGE);

		// then
		assertThat(result).isEqualTo(CREATE_LIKE_SUCCESS.getMessage());
		memesRepository.findByMemesId(memes.getId())
			.ifPresent(m -> assertThat(m.getLikeCount()).isEqualTo(2L));
	}

	@Test
	@DisplayName("DB에 존재하는 좋아요로 인한 LIKE_NOT_CREATE 반환")
	void Given_ExistLike_When_registerLike_Then_LIKE_NOT_CREATE() throws Exception {
		// given
		likeRepository.save(LIKE(member, memes));

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.post("/memes/{memesId}/like", memes.getId())
				.then()
				.log()
				.all()
				.extract();

		String result = response.jsonPath().get(ERROR_MESSAGE);

		// then
		assertThat(result).isEqualTo(LIKE_NOT_CREATE.getMessage());
	}

	@Test
	@DisplayName("memesId를 통한 좋아요 삭제 성공")
	void Given_memesId_When_cancelLike_Then_DELETE_LIKE_SUCCESS() {
		// given
		Like like = likeRepository.save(LIKE(member, memes));

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.delete("/memes/{memesId}/like", memes.getId())
				.then()
				.log()
				.all()
				.extract();

		String result = response.jsonPath().get(MESSAGE);

		// then
		assertThat(result).isEqualTo(DELETE_LIKE_SUCCESS.getMessage());
	}

	@Test
	@DisplayName("DB에 없는 좋아요로 인한 LIKE_NOT_FOUND 반환")
	void Given_NotExistLike_When_cancelLike_Then_LIKE_NOT_FOUND() {
		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.delete("/memes/{memesId}/like", memes.getId())
				.then()
				.log()
				.all()
				.extract();

		String result = response.jsonPath().get(ERROR_MESSAGE);

		// then
		assertThat(result).isEqualTo(LIKE_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("올타임 좋아요 Top 10 밈스 반환 성공")
	public void When_findTopMemesByLike_Then_GET_TOP_TEN_MEMES_SUCCESS() {
		// given
		final Long SIZE = 15L;

		for (long i = 0; i < SIZE; i++) {
			memesRepository.save(MEMES_SET_LIKE(member, meme, i));
		}

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.get("/memes/like/all")
				.then()
				.log()
				.all()
				.extract();

		List<MemesInfoResponse> responses = response.jsonPath().getList("data", MemesInfoResponse.class);

		// then
		assertThat(responses).hasSize(10);
		assertThat(responses.get(0).getLikeCount()).isEqualTo(SIZE - 1);
	}

	@Test
	@DisplayName("주간 좋아요 Top 10 밈스 반환 성공")
	public void When_findTopMemesByLikeForWeek_Then_GET_WEEK_TOP_TEN_MEMES_SUCCESS() {
		// given
		final Long SIZE = 15L;
		String dateKey = "LIKE_RANKING_DATE::" + LocalDate.now().minusDays(1);

		for (Long i = 0L; i < SIZE; i++) {
			// 총 좋아요 수와 오늘 좋아요 수의 차이를 주기 위해 + 3
			Memes rankMemes = memesRepository.save(MEMES_SET_LIKE(member, meme, i + 3));

			rankingZSet.add(dateKey, rankMemes.getId(), i);
		}

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.get("/memes/like/week")
				.then()
				.log()
				.all()
				.extract();

		List<MemesInfoResponse> responses = response.jsonPath().getList("data", MemesInfoResponse.class);

		// then
		assertThat(responses).hasSize(10);
		assertThat(responses.get(0).getLikeCount()).isEqualTo(SIZE - 1);
	}
}
