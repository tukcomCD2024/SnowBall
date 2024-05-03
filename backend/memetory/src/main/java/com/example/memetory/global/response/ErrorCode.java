package com.example.memetory.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Enum Naming Format : {주체}_{이유} message format: 동사 명사형으로 마무리 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
	;
	private final int status;
	private final String message;
}