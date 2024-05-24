package com.example.memetory.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Enum Naming Format : {주체}_{이유} message format: 동사 명사형으로 마무리 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
	// Global
	INTERNAL_SERVER_ERROR(500, "서버 오류"),
	INPUT_INVALID_VALUE(400, "잘못된 입력"),

	// Auth
	TOKEN_IS_INVALID(403, "토큰 인증 실패"),
	TOKEN_NOT_FOUND(403, "토큰 추출 실패"),
	EMAIL_NOT_FOUND(403, "이메일 추출 실패"),

	// Member
	MEMBER_NOT_FOUND(404, "멤버를 찾기 실패"),
	NICKNAME_IS_DUPLICATED(409, "닉네임 중복"),

	// Meme
	MEME_NOT_FOUND(404, "밈을 찾기 실패"),
	MEME_ACCESS_DENY(403, "밈 접근 실패"),

	// Memes
	MEMES_NOT_FOUND(404, "밈스를 찾기 실패"),
	MEMES_ACCESS_DENY(403, "밈스 접근 실패"),

	// Like
	LIKE_NOT_FOUND(404, "좋아요 찾기 실패"),
	LIKE_NOT_CREATE(409, "중복된 좋아요"),

	// Complain
	COMPLAIN_NOT_FOUND(404, "신고 찾기 실패"),

	// Comment
	COMMENT_NOT_FOUND(404, "댓글 찾기 실패"),

	// Voice
	VOICE_NOT_FOUND(404, "보이스 찾기 실패")
	;

	private final int status;
	private final String message;
}