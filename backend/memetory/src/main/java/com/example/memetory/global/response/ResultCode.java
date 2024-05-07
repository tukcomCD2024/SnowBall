package com.example.memetory.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Enum Naming Format : {행위}_{목적어}_{성공여부} message format: 동사 명사형으로 마무리 */
@Getter
@AllArgsConstructor
public enum ResultCode {
	// member
	UPDATE_MEMBER_SUCCESS(200,"멤버 업데이트 성공"),

	// meme
	CREATE_MEME_SUCCESS(201, "밈 생성 성공"),
	GET_ONE_MEME_SUCCESS(200, "단일 밈 조회 성공"),
	GET_MEMBER_MEME_SUCCESS(200, "멤버별 밈 조회 성공"),

	// memes
	CREATE_MEMES_SUCCESS(201, "밈스 생성 성공"),
	GET_ONE_MEMES_SUCCESS(200, "단일 밈스 조회 성공"),
	GET_TOP_TEN_MEMES_SUCCESS(200, "Top 10 밈스 조회 성공"),
	GET_MONTH_TOP_TEN_MEMES_SUCCESS(200, "월별 Top 10 밈스 조회 성공"),
	GET_WEEK_TOP_TEN_MEMES_SUCCESS(200, "주별 Top 10 밈스 조회 성공"),
	GET_ALL_MEMES_SUCCESS(200, "전체 밈스 조회 성공"),
	DELETE_MEMES_SUCCESS(200, "밈스 삭제 성공"),

	// like
	CREATE_LIKE_SUCCESS(201, "좋아요 생성 성공"),
	DELETE_LIKE_SUCCESS(200, "좋아요 삭제 성공"),

	// comment
	CREATE_COMMENT_SUCCESS(201, "댓글 생성 성공"),
	DELETE_COMMENT_SUCCESS(200, "댓글 삭제 성공"),

	//complain
	CREATE_COMPLAIN_SUCCESS(201, "신고 생성 성공"),
	DELETE_COMPLAIN_SUCCESS(201, "신고 삭제 성공"),

	;
	private final int status;
	private final String message;
}
