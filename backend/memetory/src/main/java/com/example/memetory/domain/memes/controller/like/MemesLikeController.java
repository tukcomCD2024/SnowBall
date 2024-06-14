package com.example.memetory.domain.memes.controller.like;

import static com.example.memetory.global.response.ResultCode.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.like.dto.LikeServiceDto;
import com.example.memetory.domain.like.service.LikeService;
import com.example.memetory.domain.memes.dto.response.MemesInfoResponse;
import com.example.memetory.domain.memes.service.MemesService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import com.example.memetory.global.response.ResultResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memes")
public class MemesLikeController implements MemesLikeApi {
	private final MemesService memesService;
	private final LikeService likeService;

	@GetMapping("/like/all")
	@Override
	public ResponseEntity<ResultResponse> findTopMemesByLike() {
		List<MemesInfoResponse> response = memesService.findTopMemesByLike();
		return ResponseEntity.ok(ResultResponse.of(GET_TOP_TEN_MEMES_SUCCESS, response));
	}

	@GetMapping("/like/month")
	@Override
	public ResponseEntity<ResultResponse> findTopMemesByLikeForMonth() {
		List<MemesInfoResponse> response = memesService.findTopMemesByLikeForMonth();
		return ResponseEntity.ok(ResultResponse.of(GET_MONTH_TOP_TEN_MEMES_SUCCESS, response));
	}

	@GetMapping("/like/week")
	@Override
	public ResponseEntity<ResultResponse> findTopMemesByLikeForWeek() {
		List<MemesInfoResponse> response = memesService.findTopMemesByLikeForWeek();
		return ResponseEntity.ok(ResultResponse.of(GET_WEEK_TOP_TEN_MEMES_SUCCESS, response));
	}

	@PostMapping("/{memesId}/like")
	@Override
	public ResponseEntity<ResultResponse> registerLike(@LoginMemberEmail String email, @PathVariable Long memesId) {
		LikeServiceDto likeServiceDto = LikeServiceDto.fromEmailAndMemesId(email, memesId);
		likeService.registerLike(likeServiceDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(CREATE_LIKE_SUCCESS));
	}

	@DeleteMapping("/{memesId}/like")
	@Override
	public ResponseEntity<ResultResponse> cancelLike(@LoginMemberEmail String email, @PathVariable Long memesId) {
		LikeServiceDto likeServiceDto = LikeServiceDto.fromEmailAndMemesId(email, memesId);
		likeService.cancelLike(likeServiceDto);

		return ResponseEntity.ok(ResultResponse.of(DELETE_LIKE_SUCCESS));
	}
}
