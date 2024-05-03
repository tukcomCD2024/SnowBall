package com.example.memetory.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse {
	private int status;
	private String message;
	private Object data;

	public static ResultResponse of(ResultCode resultCode, Object data) {
		return new ResultResponse(resultCode, data);
	}

	public ResultResponse(ResultCode resultCode, Object data) {
		this.message = resultCode.getMessage();
		this.data = data;
	}
}