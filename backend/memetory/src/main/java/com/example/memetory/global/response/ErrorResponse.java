package com.example.memetory.global.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
	private String errorMessage;
	private List<FieldError> errors;

	public ErrorResponse(ErrorCode code, List<FieldError> fieldErrors) {
		this.errorMessage = code.getMessage();
		this.errors = fieldErrors;
	}

	private ErrorResponse(ErrorCode code) {
		this.errorMessage = code.getMessage();
		this.errors = new ArrayList<>();
	}

	public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
		return new ErrorResponse(code, FieldError.of(bindingResult));
	}

	public static ErrorResponse of(ErrorCode code) {
		return new ErrorResponse(code);
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class FieldError {
		private String field;
		private String value;
		private String reason;

		public FieldError(final String field, final String value, final String reason) {
			this.field = field;
			this.value = value;
			this.reason = reason;
		}

		private static List<FieldError> of(final BindingResult bindingResult) {
			final List<org.springframework.validation.FieldError> fieldErrors =
				bindingResult.getFieldErrors();
			return fieldErrors.stream()
				.map(
					error ->
						new FieldError(
							error.getField(),
							error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
							error.getDefaultMessage()))
				.collect(Collectors.toList());
		}
	}
}

